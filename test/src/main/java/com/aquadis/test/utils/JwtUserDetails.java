package com.aquadis.test.utils;

import com.aquadis.test.entity.Customer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class JwtUserDetails implements UserDetails {

    private final Customer customer;

    // Constructor to initialize JwtUserDetails with Customer object
    public JwtUserDetails(Customer customer) {
        this.customer = customer;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // You can add roles or authorities if needed (currently returns empty list)
        return new ArrayList<>(); // Empty authority list, customize if roles exist
    }

    @Override
    public String getPassword() {
        return customer.getPassword(); // Return customer's password
    }

    @Override
    public String getUsername() {
        return customer.getName(); // Return customer's name
    }

    // Customer specific method to get customer ID
    public long getCustomerID() {
        return customer.getCustomerID(); // Assuming Customer has getCustomerID method
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Assume account is not expired (customize if necessary)
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Assume account is not locked (customize if necessary)
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Assume credentials are not expired (customize if necessary)
    }

    @Override
    public boolean isEnabled() {
        return true; // Assume the account is enabled (customize if necessary)
    }
}
