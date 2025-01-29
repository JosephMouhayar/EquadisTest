package com.aquadis.test.controllers;

import com.aquadis.test.dto.AuthenticationRequest;
import com.aquadis.test.dto.CustomerDto;
import com.aquadis.test.dto.SignUpRequest;
import com.aquadis.test.entity.Customer;
import com.aquadis.test.repository.UserRepository;
import com.aquadis.test.services.auth.AuthService;
import com.aquadis.test.services.jwt.UserDetailsServiceImpl;
import com.aquadis.test.utils.JwtUserDetails;
import com.aquadis.test.utils.JwtUtil;
import com.google.gson.Gson;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer")
public class CustomerController {

    private final AuthenticationManager authenticationManager;

    private final UserDetailsServiceImpl userDetailsService;

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String HEADER_STRING = "Authorization";

    private final AuthService authService;

    @PostMapping("/authenticate")
    public void createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) throws IOException {
        // Load user details
        final JwtUserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getName());
        Optional<Customer> optionalCustomer = userRepository.findByName(userDetails.getUsername());

        // Check if customer exists
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();

            // Hash the password from the request and compare it with the stored hashed password
            String hashedInputPassword = hashPassword(authenticationRequest.getPassword());
            if (!hashedInputPassword.equals(customer.getPassword())) {
                throw new BadCredentialsException("Incorrect customer name or password.");
            }

            // Generate JWT token if authentication is successful
            final String jwt = jwtUtil.generateToken(userDetails);

            // Prepare the response map
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("CustomerID", customer.getCustomerID());
            responseMap.put("Name", customer.getName());

            // Serialize the response map to JSON
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(responseMap);

            // Set the response content type and write the response
            response.setContentType("application/json");
            response.getWriter().write(jsonResponse);

            response.addHeader("Access-Control-Expose-Headers", "Authorization");
            response.addHeader("Access-Control-Allow-Headers", "Authorization, X-PINGOTHER, Origin, "+"(-Requested-With, Content-Type, Accept, X-Custom-header");

            // Add JWT token in the response header
            response.addHeader(HEADER_STRING, TOKEN_PREFIX + jwt);
        } else {
            throw new BadCredentialsException("Customer not found.");
        }
    }

    public static String hashPassword(String password) {
        try {
            // SHA-256 hashing of the password
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));  // Convert to hex format
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found.", e);
        }
    }

    // Customer Signup
    @PostMapping("/signup")
    @Transactional
    public ResponseEntity<?> signup(@RequestBody SignUpRequest signUpRequest) {
        if(authService.hasCustomerWithName(signUpRequest.getName())) {
            return new ResponseEntity<>("Customer already exists", HttpStatus.NOT_ACCEPTABLE);
        }

        CustomerDto customerDto = authService.createCustomer(signUpRequest);
        return new ResponseEntity<>(customerDto, HttpStatus.OK);
    }


}
