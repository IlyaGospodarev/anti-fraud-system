package antifraud.controller;

import antifraud.dto.TransactionDto;
import antifraud.model.Transaction;
import antifraud.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/antifraud/transaction")
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionDto> sendAmount(@RequestBody Transaction transaction) {
        return ResponseEntity.ok(transactionService.sendAmount(transaction));
    }
}
