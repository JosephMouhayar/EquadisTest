package com.aquadis.test.services.auth;

import com.aquadis.test.dto.CustomerDto;
import com.aquadis.test.dto.SignUpRequest;
import com.aquadis.test.entity.Customer;
import com.aquadis.test.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService{

    @Autowired
    private UserRepository userRepository;

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public CustomerDto createCustomer(SignUpRequest signUpRequest) {
        Customer customer = new Customer();

        customer.setName(signUpRequest.getName());
        customer.setPassword(hashPassword(signUpRequest.getPassword()));

        Customer createdCustomer = userRepository.save(customer);

        CustomerDto customerDto = new CustomerDto();

        customer.setCustomerID(createdCustomer.getCustomerID());

        return customerDto;
    }

    public Boolean hasCustomerWithName(String name) {
        return userRepository.findByName(name).isPresent();
    }
}
