package com.aquadis.test.repository;

import com.aquadis.test.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findByBankAccount_BankAccountID(long bankAccountID, Pageable pageable);
}
