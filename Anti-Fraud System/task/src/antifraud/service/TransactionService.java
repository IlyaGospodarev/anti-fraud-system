package antifraud.service;

import antifraud.dto.TransactionRequest;
import antifraud.dto.TransactionResponse;
import antifraud.exception.InvalidTransactionAmount;
import antifraud.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public TransactionResponse sendAmount(TransactionRequest transactionRequest) {
        long amount = transactionRequest.getAmount();

        TransactionResponse transactionResponse = new TransactionResponse();

        if (amount < 1) {
            throw new InvalidTransactionAmount();
        }

        if (amount <= 200) {
            transactionResponse.setResult("ALLOWED");
        } else if (amount <= 1500) {
            transactionResponse.setResult("MANUAL_PROCESSING");
        } else {
            transactionResponse.setResult("PROHIBITED");
        }

        return transactionResponse;
    }
}