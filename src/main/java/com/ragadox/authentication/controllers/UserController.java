package com.ragadox.authentication.controllers;

import com.ragadox.authentication.dto.ResponseDTO;
import com.ragadox.authentication.dto.UserDTO;
import com.ragadox.authentication.entity.UserEntity;
import com.ragadox.authentication.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Validated
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService=userService;
    }

    @PostMapping("/user")
    public ResponseEntity<ResponseDTO<UserEntity>> createUser(@Valid @RequestBody UserDTO userDTO) {
        return ResponseDTO.success(userService.createUser(userDTO));
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<ResponseDTO<UserEntity>> getUserByUsername(@Valid @PathVariable("username")
                                                                         @NotBlank(message = "username is required")
                                                                         @Size(min = 3, max = 20, message = "Username must be 3-20 characters")
                                                                         @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers, and underscores")
                                                                         String username){
        return ResponseDTO.success(userService.getUserByUsername(username));
    }
}
