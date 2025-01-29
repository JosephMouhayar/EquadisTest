package com.aquadis.test.controllers;

import com.aquadis.test.dto.CategoryDto;
import com.aquadis.test.dto.TransactionDto;
import com.aquadis.test.services.CategoryService;
import com.aquadis.test.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Endpoint to get all transactions for a given bank account
    @GetMapping("/getCategories")
    public ResponseEntity<List<CategoryDto>> getCategories() {
        List<CategoryDto> transactions = categoryService.getCategories();
        return ResponseEntity.ok(transactions);
    }
}
