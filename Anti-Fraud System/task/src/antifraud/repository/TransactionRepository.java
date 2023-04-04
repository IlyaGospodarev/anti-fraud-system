package antifraud.repository;

import antifraud.dto.TransactionDto;
import antifraud.exception.InvalidTransactionAmount;
import antifraud.model.Transaction;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionRepository {

    public TransactionDto checkTransactionSum(Transaction transaction) {
        int amount = transaction.getAmount();

        TransactionDto transactionDto = new TransactionDto();

        if (amount < 1) {
            throw new InvalidTransactionAmount();
        }

        if (amount <= 200) {
            transactionDto.setResult("ALLOWED");
        } else if (amount <= 1500) {
            transactionDto.setResult("MANUAL_PROCESSING");
        } else {
            transactionDto.setResult("PROHIBITED");
        }

        return transactionDto;
    }
}

