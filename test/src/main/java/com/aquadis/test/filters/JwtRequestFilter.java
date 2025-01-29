package com.aquadis.test.filters;

import com.aquadis.test.services.jwt.UserDetailsServiceImpl;
import com.aquadis.test.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImpl userDetailsService;

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String customerName = null;
        long customerID = -1;

        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);  // Remove "Bearer " prefix
            customerName = jwtUtil.extractUsername(token); // Extract username (name)
            customerID = jwtUtil.extractCustomerID(token);  // Extract customerID
        }

        if(customerName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(customerName);

            if(jwtUtil.validateToken(token, userDetails)) {
                // Create a custom authentication token including the customerID
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                // Include customerID as part of the authentication details
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // You can store the customerID in the authentication details if needed
                SecurityContextHolder.getContext().setAuthentication(authToken);

                // Optionally, you can attach the customerID to the request, so it's available in subsequent filters or services
                request.setAttribute("customerID", customerID);
            }
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }
}
