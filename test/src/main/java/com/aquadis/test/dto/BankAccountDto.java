package com.aquadis.test.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class BankAccountDto {

    private long bankAccountID;
    private float amount;
}
