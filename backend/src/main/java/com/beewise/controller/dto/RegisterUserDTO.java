package com.beewise.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDTO {

    @NotBlank(message = "Name cannot be empty")
    @Size(max = 10, message = "Name is too long")
    private String name;

    @NotBlank(message = "Surname cannot be empty")
    @Size(max = 10, message = "Surname is too long")
    private String surname;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email is not valid")
    @Size(max = 50, message = "Email is too long")
    private String email;

    @NotBlank(message = "Username cannot be empty")
    @Size(max = 10, message = "Username is too long")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 10 characters")
    private String password;
}
