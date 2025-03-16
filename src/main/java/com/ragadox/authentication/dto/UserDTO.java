package com.ragadox.authentication.dto;

import com.ragadox.authentication.entity.UserEntity;
import com.ragadox.authentication.validationGroups.SignInValidationGroup;
import com.ragadox.authentication.validationGroups.SignUpValidationGroup;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {


    private Long id;

    @NotBlank(message = "username is required", groups = {SignUpValidationGroup.class, SignInValidationGroup.class})
    @Size(min = 3, max = 20, message = "Username must be 3-20 characters", groups = {SignUpValidationGroup.class, SignInValidationGroup.class})
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers, and underscores",
            groups = {SignUpValidationGroup.class, SignInValidationGroup.class})
    private String username;

    @NotBlank(message = "email is required", groups = {SignUpValidationGroup.class})
    @Email(message = "Invalid email", groups = {SignUpValidationGroup.class})
    private String email;

    @NotBlank(message = "password is required", groups = {SignUpValidationGroup.class, SignInValidationGroup.class})
    @Size(min = 8, max = 20, message = "Password must be 6-20 characters", groups = {SignUpValidationGroup.class, SignInValidationGroup.class})
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$", message = "Password" +
            " must contain at least one uppercase letter, one lowercase letter, one number, and one special character", groups = {SignUpValidationGroup.class, SignInValidationGroup.class})
    private String password;

    public UserDTO(UserEntity userEntity) {
        this.id = userEntity.getId();
        this.username = userEntity.getUsername();
        this.email = userEntity.getEmail();
    }
}
