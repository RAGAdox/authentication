package com.ragadox.authentication.utils;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ragadox.authentication.dto.UserDTO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SecureDigestAlgorithm;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtils {

    private final RSAKeyUtils rsaKeyUtils;
    private KeyPair keyPair;
    @Value("${jwt.expiration_time}")
    private long EXPIRATION_TIME;
    private static final SecureDigestAlgorithm<PrivateKey, PublicKey> ALGORITHM = Jwts.SIG.RS256;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtUtils(RSAKeyUtils rsaKeyUtils) {
        this.rsaKeyUtils = rsaKeyUtils;
    }

    @PostConstruct
    private void init() {
        keyPair = rsaKeyUtils.getKeyPair();
    }

    public String generateToken(UserDTO userDTO) throws JsonProcessingException {
        String userJSON = objectMapper.writeValueAsString(userDTO);
        Map<String, Object> claims = new HashMap<>();
        claims.put("user", userJSON);
        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(keyPair.getPrivate(), ALGORITHM)
                .compact();
    }

    public UserDTO getUserFromToken(String token) throws JsonProcessingException {
        Claims claims = Jwts.parser()
                .verifyWith(keyPair.getPublic())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        String userJSON = claims.get("user", String.class);
        return objectMapper.readValue(userJSON, UserDTO.class);
    }

    public Boolean validateToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(keyPair.getPublic())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration()
                    .after(new Date());
        } catch (Exception e) {
            return false;
        }

    }
}
