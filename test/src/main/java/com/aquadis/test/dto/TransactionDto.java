package com.aquadis.test.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionDto {

    private long transactionID;
    private String type;
    private float amount;
    private LocalDateTime createdAt;
    private long bankAccountID;
    private long categoryID;
    private String categoryName;
}
