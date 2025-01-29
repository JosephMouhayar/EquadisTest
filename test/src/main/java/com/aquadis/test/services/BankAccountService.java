package com.aquadis.test.services;

import com.aquadis.test.dto.BankAccountDto;
import com.aquadis.test.dto.TransactionDto;
import com.aquadis.test.entity.BankAccount;
import com.aquadis.test.entity.Customer;
import com.aquadis.test.entity.Transaction;
import com.aquadis.test.repository.BankAccountRepository;
import com.aquadis.test.repository.UserRepository; // Add this import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private UserRepository customerRepository; // Inject the CustomerRepository

    // Convert Entity to DTO
    private BankAccountDto convertToDTO(BankAccount bankAccount) {
        BankAccountDto dto = new BankAccountDto();
        dto.setBankAccountID(bankAccount.getBankAccountID());
        dto.setAmount(bankAccount.getAmount());
        return dto;
    }

    // Get All Bank Accounts for a given customer ID
    public List<BankAccountDto> getBankAccountsByCustomerId(Long customerID) {
        // Fetch all bank accounts for a given customerID
        List<BankAccount> bankAccounts = bankAccountRepository.findByCustomer_CustomerID(customerID); // Corrected method name
        return bankAccounts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Initialize Bank Account for a given customer ID with an amount of 0
    public BankAccountDto initializeBankAccount(Long customerID) {
        Optional<Customer> customerOpt = customerRepository.findById(customerID);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            BankAccount bankAccount = new BankAccount();
            bankAccount.setAmount(0); // Initialize with 0 amount
            bankAccount.setCustomer(customer); // Set the customer
            BankAccount savedBankAccount = bankAccountRepository.save(bankAccount); // Save the new bank account
            return convertToDTO(savedBankAccount); // Convert and return as DTO
        } else {
            throw new RuntimeException("Customer not found"); // Throw an exception if customer not found
        }
    }

    // Delete Bank Account by ID
    public void deleteBankAccount(Long bankAccountID) {
        // Delete bank account by ID
        bankAccountRepository.deleteById(bankAccountID);
    }
}
