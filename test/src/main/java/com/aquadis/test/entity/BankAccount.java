package com.aquadis.test.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table (name = "BankAccounts")
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BankAccountID")  // SQL column is in uppercase
    private long bankAccountID;

    @Column(name = "Amount")  // SQL column is in uppercase
    private float amount;

    @ManyToOne
    @JoinColumn(name = "CustomerID")  // assuming you have a customer entity
    private Customer customer;

    @OneToMany(mappedBy = "bankAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions;
}
