package antifraud.service;

import antifraud.dto.TransactionDto;
import antifraud.model.Transaction;
import antifraud.repository.TransactionRepository;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public TransactionDto sendAmount(Transaction transaction) {
        return transactionRepository.checkTransactionSum(transaction);
    }
}
