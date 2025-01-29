package com.aquadis.test.services.auth;

import com.aquadis.test.dto.CustomerDto;
import com.aquadis.test.dto.SignUpRequest;

public interface AuthService {

    CustomerDto createCustomer(SignUpRequest signUpRequest);

    Boolean hasCustomerWithName(String customerName);
}
