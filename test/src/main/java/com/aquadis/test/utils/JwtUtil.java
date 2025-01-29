package com.aquadis.test.utils;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "s3cUr3!JWT$K3y@2024#%gH9pLxYtQwMz"; // Your secret key
    private static final long JWT_TOKEN_VALIDITY = 10 * 60 * 60 * 1000; // 10 hours in milliseconds

    // Extract the username (customer name) from the JWT token
    public String extractUsername(String token) {
        return extractClaim(token, "sub");  // Extract subject claim (customer name)
    }

    // Extract customer ID from the JWT token
    public long extractCustomerID(String token) {
        return Long.parseLong(extractClaim(token, "customerID"));  // Extract customerID claim
    }

    // Extract the expiration date from the JWT token
    public Date extractExpiration(String token) {
        return new Date(Long.parseLong(extractClaim(token, "exp")));  // Extract expiration claim
    }

    // Generic method to extract any claim based on key
    public String extractClaim(String token, String claimKey) {
        String[] parts = token.split("\\.");  // Split token into three parts: header, payload, signature
        String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);

        // Manually parse the payload JSON to extract the claim value
        String[] keyValuePairs = payload.replace("{", "").replace("}", "").split(",");
        for (String pair : keyValuePairs) {
            String[] keyValue = pair.split(":");
            if (keyValue[0].replace("\"", "").equals(claimKey)) {
                return keyValue[1].replace("\"", "");
            }
        }
        return null;  // Return null if claim is not found
    }

    // Check if the token has expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Generate JWT token for a given UserDetails (JwtUserDetails in this case)
    public String generateToken(UserDetails userDetails) {
        if (userDetails instanceof JwtUserDetails) {
            JwtUserDetails jwtUserDetails = (JwtUserDetails) userDetails;  // Safely cast to JwtUserDetails
            Map<String, Object> claims = new HashMap<>();
            claims.put("customerID", jwtUserDetails.getCustomerID());  // Add customerID as a claim
            claims.put("name", jwtUserDetails.getUsername());  // Add customerName (username) as a claim
            return createToken(claims, jwtUserDetails.getUsername());
        }
        throw new IllegalArgumentException("UserDetails must be an instance of JwtUserDetails");
    }

    // Create the JWT token with claims and expiration time manually
    private String createToken(Map<String, Object> claims, String subject) {
        try {
            // Step 1: Create the JWT Header
            String header = createHeader();

            // Step 2: Create the Payload (Claims)
            String payload = createPayload(claims, subject);

            // Step 3: Combine the header and payload
            String base64Header = encodeBase64Url(header);
            String base64Payload = encodeBase64Url(payload);

            // Step 4: Create the Signature
            String signature = createSignature(base64Header, base64Payload);

            // Step 5: Combine all parts to form the JWT
            return base64Header + "." + base64Payload + "." + signature;
        } catch (Exception e) {
            throw new RuntimeException("Error creating token", e);
        }
    }

    // Encode data to Base64 URL encoding
    private String encodeBase64Url(String data) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(data.getBytes(StandardCharsets.UTF_8));
    }

    // Create JWT Header
    private String createHeader() {
        // JSON header with the signing algorithm used (e.g., HS256)
        return "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
    }

    // Create JWT Payload with claims and expiration time
    private String createPayload(Map<String, Object> claims, String subject) {
        long expirationTime = System.currentTimeMillis() + JWT_TOKEN_VALIDITY;

        // Create the payload (JSON object with claims)
        StringBuilder payloadBuilder = new StringBuilder();
        payloadBuilder.append("{");

        // Add subject
        payloadBuilder.append("\"sub\":\"").append(subject).append("\",");

        // Add claims (customerID, Name, etc.)
        claims.forEach((key, value) -> payloadBuilder.append("\"").append(key).append("\":\"").append(value).append("\","));

        // Add expiration time
        payloadBuilder.append("\"exp\":").append(expirationTime);

        payloadBuilder.append("}");

        return payloadBuilder.toString();
    }

    // Create JWT Signature using HMAC SHA-256
    private String createSignature(String base64Header, String base64Payload) throws Exception {
        try {
            // Combine header and payload
            String data = base64Header + "." + base64Payload;

            // Create the HMAC SHA-256 signature
            Mac hmacSha256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            hmacSha256.init(secretKeySpec);
            byte[] signatureBytes = hmacSha256.doFinal(data.getBytes(StandardCharsets.UTF_8));

            // Return the Base64 URL encoded signature
            return Base64.getUrlEncoder().withoutPadding().encodeToString(signatureBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error creating signature", e);
        }
    }

    // Validate the JWT token against the UserDetails (JwtUserDetails in this case)
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));  // Validate username and expiry
    }
}
