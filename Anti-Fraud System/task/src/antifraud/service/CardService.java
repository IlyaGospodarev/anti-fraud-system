package antifraud.service;

import antifraud.dto.CardRequest;
import antifraud.dto.CardResponse;
import antifraud.dto.StatusResponse;
import antifraud.exception.BadRequestException;
import antifraud.exception.HttpConflictException;
import antifraud.exception.NotFoundException;
import antifraud.model.Card;
import antifraud.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CardService {
    @Autowired
    CardRepository cardRepository;

    public CardResponse addCard(CardRequest cardRequest) {
        Optional<Card> optionalCard = cardRepository.findByNumber(cardRequest.getNumber());
        if (optionalCard.isPresent()) {
            throw new HttpConflictException("Stolen card already in the database");
        }

        boolean validCreditCard = validateCreditCard(cardRequest.getNumber());

        if (!validCreditCard) {
            throw new BadRequestException("Invalid Card format");
        }

        Card card = new Card();
        card.setNumber(cardRequest.getNumber());
        cardRepository.save(card);

        return new CardResponse(card);

    }

    public StatusResponse deleteStolenCard(String number) {

        boolean validCreditCard = validateCreditCard(number);

        if (!validCreditCard) {
            throw new BadRequestException("Invalid Card format");
        }

        Optional<Card> optionalCard = cardRepository.findByNumber(number);

        if (optionalCard.isPresent()) {
            Card card = optionalCard.get();
            cardRepository.delete(card);
            return new StatusResponse(String.format("Card %s successfully removed!", card.getNumber()));
        } else {
            throw new NotFoundException("Card number is not found in the database");
        }
    }

    public List<CardResponse> getStolenCards() {
        return cardRepository.findAll().stream().map(CardResponse::new).collect(Collectors.toList());
    }

    private boolean validateCreditCard(String cardNum) {

        int nDigits = cardNum.length();
        int nSum = 0;
        for (int i = 0; i < nDigits; i++) {
            int d = cardNum.charAt(i) - '0';
            if (i % 2 == 0) {
                d *= 2;
                nSum += d / 10;
                nSum += d % 10;
            } else {
                nSum += d;
            }
        }
        return nSum % 10 == 0;
    }
}
