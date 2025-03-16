package com.ragadox.authentication.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ragadox.authentication.dto.ResponseDTO;
import com.ragadox.authentication.dto.SuccessResponseBuilderDTO;
import com.ragadox.authentication.dto.UserDTO;
import com.ragadox.authentication.exceptions.JWTAuthenticationException;
import com.ragadox.authentication.service.UserService;
import com.ragadox.authentication.utils.ResponseCookieUtils;
import com.ragadox.authentication.validationGroups.SignInValidationGroup;
import com.ragadox.authentication.validationGroups.SignUpValidationGroup;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<ResponseDTO<UserDTO>> signUp(@Validated({SignUpValidationGroup.class}) @RequestBody UserDTO userDTO) {
        return SuccessResponseBuilderDTO.withData(userService.createUser(userDTO)).build();
    }

    @PostMapping("/sign-in")
    public ResponseEntity<ResponseDTO<UserDTO>> signIn(@Validated({SignInValidationGroup.class}) @RequestBody UserDTO userDTO) throws JsonProcessingException {
        UserDTO verifiedUser = userService.verifyCredentials(userDTO);
        String token = userService.getToken(verifiedUser);

        return SuccessResponseBuilderDTO
                .withData(userService.verifyCredentials(userDTO))
                .withCookies(ResponseCookieUtils.getCookie("token",token))
                .build();
    }

    @GetMapping("/current")
    public ResponseEntity<ResponseDTO<UserDTO>> getCurrentUser() throws JsonProcessingException {
        Authentication authenticationToken =
                SecurityContextHolder.getContext().getAuthentication();
        Object principal = authenticationToken.getPrincipal();
        if( principal instanceof UserDTO userDTO){
            String token = userService.getToken(userDTO);
            return SuccessResponseBuilderDTO
                    .withData(userDTO)
                    .withCookies(ResponseCookieUtils.getCookie("token",token))
                    .build();
        }
        throw new JWTAuthenticationException("Invalid credentials");
    }

    @GetMapping("/error")
    public ResponseEntity<ResponseDTO<Object>> error(HttpServletRequest request) throws Exception {
        Object exceptionObject = request.getAttribute("exception");
        if (exceptionObject instanceof Exception exception) {
            throw exception;
        }
        throw new RuntimeException();
    }

}
