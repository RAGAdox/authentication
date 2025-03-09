package com.ragadox.authentication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public record UsernameDTO(@NotBlank(message = "username is required")
                          @Size(min = 3, max = 20, message = "Username must be 3-20 characters")
                          @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers, and underscores")
                          String value) {}