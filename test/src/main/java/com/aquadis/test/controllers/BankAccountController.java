package com.aquadis.test.controllers;

import com.aquadis.test.dto.BankAccountDto;
import com.aquadis.test.services.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bankAccounts")
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;

    // Endpoint to get all bank accounts for a given customer ID
    @GetMapping("/getBankAccounts")
    public ResponseEntity<List<BankAccountDto>> getBankAccountsByCustomerId(@RequestParam Long customerID) {
        List<BankAccountDto> bankAccounts = bankAccountService.getBankAccountsByCustomerId(customerID);
        return ResponseEntity.ok(bankAccounts);
    }

    // Endpoint to initialize a new bank account for a given customer with an amount of 0
    @PostMapping("/initializeBankAccount")
    public ResponseEntity<BankAccountDto> initializeBankAccount(@RequestParam Long customerID) {
        BankAccountDto bankAccount = bankAccountService.initializeBankAccount(customerID);
        return ResponseEntity.ok(bankAccount);
    }

    // Endpoint to delete a bank account by its ID
    @DeleteMapping("/deleteBankAccount")
    public ResponseEntity<Void> deleteBankAccount(@RequestParam Long bankAccountID) {
        bankAccountService.deleteBankAccount(bankAccountID);
        return ResponseEntity.noContent().build();  // Respond with 204 No Content
    }
}
