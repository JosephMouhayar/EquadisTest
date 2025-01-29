package com.aquadis.test.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class CategoryDto {

    private long categoryID;
    private String name;
}
