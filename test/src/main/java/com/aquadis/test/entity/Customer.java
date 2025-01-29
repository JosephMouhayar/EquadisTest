package com.aquadis.test.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "Customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CustomerID")  // SQL column is in uppercase
    private Long customerID;  // Java field is in lowercase

    @Column(name = "Name")  // SQL column is in uppercase
    private String name;     // Java field is in lowercase

    @Column(name = "Password")  // SQL column is in uppercase
    private String password;    // Java field is in lowercase

    @OneToMany(mappedBy = "customer")
    private List<BankAccount> bankAccounts;
}
