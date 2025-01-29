package com.aquadis.test.services.jwt;

import com.aquadis.test.entity.Customer;
import com.aquadis.test.repository.UserRepository;
import com.aquadis.test.utils.JwtUserDetails;  // Assuming JwtUserDetails extends User and implements UserDetails
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public JwtUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Retrieve customer from the repository
        Optional<Customer> optionalCustomer = userRepository.findByName(username);

        // Provide more detailed information in the exception for better debugging
        if (optionalCustomer.isEmpty()) {
            throw new UsernameNotFoundException("Customer with username: " + username + " not found");
        }

        Customer customer = optionalCustomer.get();

        // Construct JwtUserDetails object and return it
        return new JwtUserDetails(customer); // Assuming no roles for now
    }
}