package com.aquadis.test.services;

import com.aquadis.test.dto.TransactionDto;
import com.aquadis.test.entity.Transaction;
import com.aquadis.test.repository.TransactionRepository;
import com.aquadis.test.entity.BankAccount;
import com.aquadis.test.entity.Category;
import com.aquadis.test.repository.BankAccountRepository;
import com.aquadis.test.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final BankAccountRepository bankAccountRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository,
                              BankAccountRepository bankAccountRepository,
                              CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.categoryRepository = categoryRepository;
    }

    public Page<TransactionDto> getTransactionsByBankAccount(long bankAccountID, int page, int size, String sortBy, String sortDir) {
        Sort sort = Sort.by(sortDir.equalsIgnoreCase("desc") ? Sort.Order.desc(sortBy) : Sort.Order.asc(sortBy));
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Transaction> transactionsPage = transactionRepository.findByBankAccount_BankAccountID(bankAccountID, pageable);

        return transactionsPage.map(this::convertToDto);
    }


    // Add a new transaction and update bank account balance
    public TransactionDto addTransaction(TransactionDto transactionDto) {
        // Fetch BankAccount and Category from the repository using their IDs
        BankAccount bankAccount = bankAccountRepository.findById(transactionDto.getBankAccountID())
                .orElseThrow(() -> new RuntimeException("Bank account not found"));
        Category category = categoryRepository.findById(transactionDto.getCategoryID())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Check if the transaction would make the bank account balance negative
        if (transactionDto.getType().equals("expense") &&
                bankAccount.getAmount() < transactionDto.getAmount()) {
            throw new RuntimeException("Transaction failed: Bank account cannot have a negative balance.");
        }

        // Create a new Transaction entity
        Transaction transaction = new Transaction();
        transaction.setType(transactionDto.getType());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setBankAccount(bankAccount);
        transaction.setCategory(category);

        // Update bank account amount
        if (transactionDto.getType().equals("income")) {
            bankAccount.setAmount(bankAccount.getAmount() + transactionDto.getAmount());
        } else if (transactionDto.getType().equals("expense")) {
            bankAccount.setAmount(bankAccount.getAmount() - transactionDto.getAmount());
        }

        // Save updated bank account
        bankAccountRepository.save(bankAccount);
        // Save the transaction
        Transaction savedTransaction = transactionRepository.save(transaction);

        return convertToDto(savedTransaction);
    }

    // Delete a transaction and update bank account balance
    public void deleteTransaction(long transactionID) {
        Transaction transaction = transactionRepository.findById(transactionID)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        BankAccount bankAccount = transaction.getBankAccount();

        // Check if deleting an income transaction would make the balance negative
        if (transaction.getType().equals("income") &&
                bankAccount.getAmount() < transaction.getAmount()) {
            throw new RuntimeException("Transaction deletion failed: Bank account cannot have a negative balance.");
        }

        // Update bank account amount
        if (transaction.getType().equals("income")) {
            bankAccount.setAmount(bankAccount.getAmount() - transaction.getAmount());
        } else if (transaction.getType().equals("expense")) {
            bankAccount.setAmount(bankAccount.getAmount() + transaction.getAmount());
        }

        // Save updated bank account
        bankAccountRepository.save(bankAccount);
        // Delete the transaction
        transactionRepository.deleteById(transactionID);
    }

    // Helper method to convert Transaction entity to TransactionDto
    private TransactionDto convertToDto(Transaction transaction) {
        TransactionDto dto = new TransactionDto();
        dto.setTransactionID(transaction.getTransactionID());
        dto.setType(transaction.getType());
        dto.setAmount(transaction.getAmount());
        dto.setCreatedAt(transaction.getCreatedAt());
        dto.setBankAccountID(transaction.getBankAccount().getBankAccountID());
        dto.setCategoryID(transaction.getCategory().getCategoryID());
        dto.setCategoryName(transaction.getCategory().getName());
        return dto;
    }
}
