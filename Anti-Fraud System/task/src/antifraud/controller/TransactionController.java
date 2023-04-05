package antifraud.controller;

import antifraud.dto.TransactionRequest;
import antifraud.dto.TransactionResponse;
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
    public ResponseEntity<TransactionResponse> sendAmount(@RequestBody TransactionRequest transactionRequest) {
        return ResponseEntity.ok(transactionService.sendAmount(transactionRequest));
    }
}
