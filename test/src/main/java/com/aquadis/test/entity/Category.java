package com.aquadis.test.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
@Table(name = "Categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CategoryID")  // SQL column is in uppercase
    private long categoryID;

    @Column(name = "Name")  // SQL column is in uppercase
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Transaction> transactions;
}
