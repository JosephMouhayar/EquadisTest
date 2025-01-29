package com.aquadis.test.repository;

import com.aquadis.test.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    List<BankAccount> findByCustomer_CustomerID(Long customerID); // Correct reference to customerID
}

