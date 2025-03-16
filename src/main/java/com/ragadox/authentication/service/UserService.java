package com.ragadox.authentication.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ragadox.authentication.dto.UserDTO;
import com.ragadox.authentication.entity.UserEntity;
import com.ragadox.authentication.exceptions.BadRequestException;
import com.ragadox.authentication.exceptions.JWTAuthenticationException;
import com.ragadox.authentication.exceptions.ResourceAlreadyExists;
import com.ragadox.authentication.repository.UserRepository;
import com.ragadox.authentication.utils.JwtUtils;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    public UserDTO createUser(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity(userDTO);
        if (userDTO.getPassword() == null) {
            throw new BadRequestException("Password is required");
        }
        Boolean isUsernameTaken = userRepository.existsByUsername(userEntity.getUsername());
        Boolean isEmailTaken = userRepository.existsByEmail(userEntity.getEmail());
        if (isUsernameTaken || isEmailTaken) {
            throw new ResourceAlreadyExists(UserEntity.class);
        }
        String encodedPassword = bCryptPasswordEncoder.encode(userDTO.getPassword());
        userEntity.setPasswordHash(encodedPassword);
        UserEntity newUserEntity = userRepository.save(userEntity);
        return newUserEntity.toDTO();
    }

    public String getToken(UserDTO userDTO) throws JsonProcessingException {
        return jwtUtils.generateToken(userDTO);
    }

    public UserDTO verifyCredentials(UserDTO userDTO) {
        UserEntity userEntity =
                userRepository.findByUsername(userDTO.getUsername()).orElseThrow(() -> new JWTAuthenticationException("Invalid credentials"));
        if (!bCryptPasswordEncoder.matches(userDTO.getPassword(), userEntity.getPasswordHash())) {
            throw new BadCredentialsException("Invalid credentials");
        }
        return userEntity.toDTO();
    }
}
