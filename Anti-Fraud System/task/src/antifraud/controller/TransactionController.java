package antifraud.controller;

import antifraud.dto.*;
import antifraud.model.Transaction;
import antifraud.service.CardService;
import antifraud.service.IpAddressService;
import antifraud.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/antifraud")
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    @Autowired
    IpAddressService ipAddressService;

    @Autowired
    CardService cardService;

    @PostMapping("/transaction")
    public ResponseEntity<TransactionResponse> sendAmount(@RequestBody @Valid TransactionRequest transactionRequest) {
        return ResponseEntity.ok(transactionService.processTransaction(transactionRequest));
    }

    @PostMapping("/suspicious-ip")
    public ResponseEntity<IpResponse> addSuspiciousIP(@RequestBody @Valid IpRequest ipRequest) {
        return ResponseEntity.ok(ipAddressService.addIpAddress(ipRequest));
    }

    @DeleteMapping("/suspicious-ip/{ip}")
    public ResponseEntity<StatusResponse> deleteSuspiciousIP(@PathVariable String ip) {
        return ResponseEntity.ok(ipAddressService.deleteSuspiciousIP(ip));
    }

    @GetMapping("/suspicious-ip")
    public List<IpResponse> getAllIPs() {
        return ipAddressService.getAllSuspiciousIPs();
    }

    @PostMapping("/stolencard")
    public ResponseEntity<CardResponse> addStolenCard(@RequestBody @Valid CardRequest cardRequest) {
      return ResponseEntity.ok(cardService.addCard(cardRequest));
    }

    @DeleteMapping("/stolencard/{number}")
    public ResponseEntity<StatusResponse> deleteStolenCard(@PathVariable String number) {
        return ResponseEntity.ok(cardService.deleteStolenCard(number));
    }

    @GetMapping("/stolencard")
    public List<CardResponse> getAllStolenCards() {
        return cardService.getStolenCards();
    }

    @PutMapping("/transaction")
    public ResponseEntity<Transaction> giveFeedbackToTransaction(@RequestBody FeedbackRequest feedbackRequest) {
        return ResponseEntity.ok(transactionService.giveFeedbackToTransaction(feedbackRequest));
    }

    @GetMapping("/history")
    public ResponseEntity<List<Transaction>> showAllTransactions() {
        return ResponseEntity.ok(transactionService.getTransactionHistory());
    }

    @GetMapping("/history/{number}")
    public ResponseEntity<List<Transaction>> showAllTransactionsByCardNumber(@PathVariable String number) {
        return ResponseEntity.ok(transactionService.getTransactionHistoryByCardNumber(number));
    }
}
