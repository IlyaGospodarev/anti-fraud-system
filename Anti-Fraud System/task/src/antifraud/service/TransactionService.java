package antifraud.service;

import antifraud.dto.FeedbackRequest;
import antifraud.dto.TransactionRequest;
import antifraud.dto.TransactionResponse;
import antifraud.exception.BadRequestException;
import antifraud.exception.HttpConflictException;
import antifraud.exception.NotFoundException;
import antifraud.exception.UnprocessableEntityException;
import antifraud.model.*;
import antifraud.repository.CardRepository;
import antifraud.repository.IpAddressRepository;
import antifraud.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    IpAddressRepository ipAddressRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    CardAmountLimitsService cardAmountLimitsService;

    public TransactionResponse processTransaction(TransactionRequest transactionRequest) {
        long amount = transactionRequest.getAmount();
        String ip = transactionRequest.getIp();
        String number = transactionRequest.getNumber();
        Region region = Region.valueOf(transactionRequest.getRegion());
        LocalDateTime dateTime = LocalDateTime.parse(transactionRequest.getDate());

        List<String> prohibitedReasons = new ArrayList<>();
        List<String> manualProcessingReasons = new ArrayList<>();

        int validateAmount = validateAmount(amount, number);
        boolean validateIP = validateIP(ip);
        boolean validateCard = validateCard(number);
        long numberOfTransactionsForCardPerRegionInLastHour =
                getNumberOfTransactionRegionLastHour(number, region, dateTime);
        long numberOfTransactionsForCardPerIpInLastHour =
                getNumberOfTransactionIpLastHour(number, ip, dateTime);

        Transaction newTransaction = new Transaction(transactionRequest);

        if (amount < 1) {
            throw new BadRequestException("The transaction amount must be greater than 0");
        }

        if (validateAmount == 2) {
            manualProcessingReasons.add("amount");
        }

        if (validateAmount == 3) {
            prohibitedReasons.add("amount");
        }

        if (!validateCard) {
            prohibitedReasons.add("card-number");
        }

        if (!validateIP) {
            prohibitedReasons.add("ip");
        }

        if (numberOfTransactionsForCardPerIpInLastHour == 2) {
            manualProcessingReasons.add("ip-correlation");
        }

        if (numberOfTransactionsForCardPerIpInLastHour > 2) {
            prohibitedReasons.add("ip-correlation");
        }

        if (numberOfTransactionsForCardPerRegionInLastHour == 2) {
            manualProcessingReasons.add("region-correlation");
        }

        if (numberOfTransactionsForCardPerRegionInLastHour > 2) {
            prohibitedReasons.add("region-correlation");
        }

        String infoManual = String.join(", ", manualProcessingReasons);
        String infoProhibited = String.join(", ", prohibitedReasons);


        if (validateAmount == 1 && prohibitedReasons.isEmpty() && manualProcessingReasons.isEmpty()) {
            newTransaction.setResult(TransactionStatus.ALLOWED.toString());
            transactionRepository.save(newTransaction);
            return new TransactionResponse(TransactionStatus.ALLOWED, "none");
        } else if (validateIP && validateCard && (validateAmount == 2 ||
                numberOfTransactionsForCardPerRegionInLastHour == 2 ||
                numberOfTransactionsForCardPerIpInLastHour == 2)) {
            newTransaction.setResult(TransactionStatus.MANUAL_PROCESSING.toString());
            transactionRepository.save(newTransaction);
            return new TransactionResponse(TransactionStatus.MANUAL_PROCESSING, infoManual);
        } else {
            newTransaction.setResult(TransactionStatus.PROHIBITED.toString());
            transactionRepository.save(newTransaction);
            return new TransactionResponse(TransactionStatus.PROHIBITED, infoProhibited);
        }
    }

    private int validateAmount(Long amount, String cardNumber) {
        return cardAmountLimitsService.processAmount(amount, cardNumber);
    }

    private boolean validateIP(String ip) {
        Optional<IpAddress> optionalIpAddress = ipAddressRepository.findByIp(ip);
        return optionalIpAddress.isEmpty();
    }

    private boolean validateCard(String number) {
        Optional<Card> optionalCard = cardRepository.findByNumber(number);
        return optionalCard.isEmpty();
    }

    private long getNumberOfTransactionRegionLastHour(String cardNumber, Region region, LocalDateTime time) {
        List<Transaction> transactionList =
                transactionRepository.findByNumberAndDateBetween(cardNumber, time.minusHours(1), time);

        return transactionList.stream()
                .map(Transaction::getRegion)
                .filter(transactionRegion -> !transactionRegion.equals(region))
                .distinct()
                .count();
    }

    private long getNumberOfTransactionIpLastHour(String cardNumber, String ip, LocalDateTime time) {
        List<Transaction> transactionList =
                transactionRepository.findByNumberAndDateBetween(cardNumber, time.minusHours(1), time);

        return transactionList.stream()
                .map(Transaction::getIp)
                .filter(transactionIp -> !transactionIp.equals(ip))
                .distinct()
                .count();
    }

    public List<Transaction> getTransactionHistory() {
        return transactionRepository.findAll();
    }

    public Transaction giveFeedbackToTransaction(FeedbackRequest feedbackRequest) {
        String requestFeedback = feedbackRequest.getFeedback()
                .toString();

        Optional<Transaction> optionalTransaction = transactionRepository.findById(feedbackRequest.getTransactionId());

        if (optionalTransaction.isPresent()) {
            Transaction transaction = optionalTransaction.get();
            String feedback = transaction.getFeedback();
            String result = transaction.getResult();
            String number = transaction.getNumber();
            Long amount = transaction.getAmount();

            if (!feedback.isEmpty()) {
                throw new HttpConflictException("Feedback already given");
            }

            if (result.equals(requestFeedback)) {
                throw new UnprocessableEntityException("Provided feedback not allowed");
            }

            cardAmountLimitsService.processLimits(number, amount, result, requestFeedback);
            transaction.setFeedback(feedbackRequest.getFeedback()
                                            .toString());
            transactionRepository.save(transaction);
            return transaction;
        } else throw new NotFoundException("Transaction is not found in history");
    }

    public List<Transaction> getTransactionHistoryByCardNumber(String number) {

        boolean validCreditCard = validateCreditCard(number);

        if (!validCreditCard) {
            throw new BadRequestException("Invalid Card format");
        }

        List<Transaction> transactionsByNumber = transactionRepository.findByNumber(number);

        if (transactionsByNumber.isEmpty()) {
            throw new NotFoundException("Card number - " + number + " not found!");
        }

        return transactionsByNumber;
    }

    private boolean validateCreditCard(String cardNum) {
        // using Luhn algorithm
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