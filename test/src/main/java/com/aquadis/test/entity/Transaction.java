package com.aquadis.test.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "Transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TransactionID")  // SQL column is in uppercase
    private long transactionID;

    @Column(name = "Type")  // SQL column is in uppercase
    private String type;

    @Column(name = "Amount")  // SQL column is in uppercase
    private float amount;

    @Column(name = "CreatedAt")  // SQL column is in uppercase
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "BankAccountID", nullable = false)
    private BankAccount bankAccount;

    @ManyToOne
    @JoinColumn(name = "CategoryID", nullable = false)
    private Category category;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

