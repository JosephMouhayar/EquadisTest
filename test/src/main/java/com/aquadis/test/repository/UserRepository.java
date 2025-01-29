package com.aquadis.test.repository;

import com.aquadis.test.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByName(String name);  // Ensure 'Name' matches the field in the entity

}

