package com.aquadis.test.controllers;

import com.aquadis.test.dto.TransactionDto;
import com.aquadis.test.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/getTransactions/{bankAccountID}")
    public ResponseEntity<Page<TransactionDto>> getTransactions(
            @PathVariable long bankAccountID,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Page<TransactionDto> transactions = transactionService.getTransactionsByBankAccount(bankAccountID, page, size, sortBy, sortDir);
        return ResponseEntity.ok(transactions);
    }

    // Endpoint to add a new transaction
    @PostMapping("/add")
    public ResponseEntity<TransactionDto> addTransaction(@RequestBody TransactionDto transactionDto) {
        TransactionDto savedTransaction = transactionService.addTransaction(transactionDto);
        return new ResponseEntity<>(savedTransaction, HttpStatus.CREATED);
    }

    // Endpoint to delete a transaction
    @DeleteMapping("/delete/{transactionID}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable long transactionID) {
        transactionService.deleteTransaction(transactionID);
        return ResponseEntity.noContent().build();
    }
}
