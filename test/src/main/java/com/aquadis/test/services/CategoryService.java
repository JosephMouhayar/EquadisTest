package com.aquadis.test.services;

import com.aquadis.test.dto.CategoryDto;
import com.aquadis.test.dto.TransactionDto;
import com.aquadis.test.entity.Transaction;
import com.aquadis.test.repository.TransactionRepository;
import com.aquadis.test.entity.BankAccount;
import com.aquadis.test.entity.Category;
import com.aquadis.test.repository.BankAccountRepository;
import com.aquadis.test.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final TransactionRepository transactionRepository;
    private final BankAccountRepository bankAccountRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(TransactionRepository transactionRepository,
                              BankAccountRepository bankAccountRepository,
                              CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.categoryRepository = categoryRepository;
    }

    // Retrieve all Categories
    public List<CategoryDto> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Helper method to convert Category entity to CategoryDto
    private CategoryDto convertToDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setCategoryID(category.getCategoryID());
        dto.setName(category.getName());
        return dto;
    }
}
