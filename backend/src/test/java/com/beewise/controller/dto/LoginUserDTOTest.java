package com.beewise.controller.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LoginUserDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void constructor_createsEmptyObject() {
        // Act
        LoginUserDTO dto = new LoginUserDTO();

        // Assert
        assertNull(dto.getUsername());
        assertNull(dto.getPassword());
    }

    @Test
    void setUsername_setsValueCorrectly() {
        // Arrange
        LoginUserDTO dto = new LoginUserDTO();
        String username = "testUser";

        // Act
        dto.setUsername(username);

        // Assert
        assertEquals(username, dto.getUsername());
    }

    @Test
    void setPassword_setsValueCorrectly() {
        // Arrange
        LoginUserDTO dto = new LoginUserDTO();
        String password = "testPassword123";

        // Act
        dto.setPassword(password);

        // Assert
        assertEquals(password, dto.getPassword());
    }

    @Test
    void validation_withValidData_passesValidation() {
        // Arrange
        LoginUserDTO dto = new LoginUserDTO();
        dto.setUsername("validUser");
        dto.setPassword("validPassword123");

        // Act
        Set<ConstraintViolation<LoginUserDTO>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty());
    }

    @Test
    void validation_withNullUsername_failsValidation() {
        // Arrange
        LoginUserDTO dto = new LoginUserDTO();
        dto.setUsername(null);
        dto.setPassword("validPassword123");

        // Act
        Set<ConstraintViolation<LoginUserDTO>> violations = validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
        ConstraintViolation<LoginUserDTO> violation = violations.iterator().next();
        assertEquals("Username cannot be empty", violation.getMessage());
        assertEquals("username", violation.getPropertyPath().toString());
    }

    @Test
    void validation_withEmptyUsername_failsValidation() {
        // Arrange
        LoginUserDTO dto = new LoginUserDTO();
        dto.setUsername("");
        dto.setPassword("validPassword123");

        // Act
        Set<ConstraintViolation<LoginUserDTO>> violations = validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
        ConstraintViolation<LoginUserDTO> violation = violations.iterator().next();
        assertEquals("Username cannot be empty", violation.getMessage());
        assertEquals("username", violation.getPropertyPath().toString());
    }

    @Test
    void validation_withBlankUsername_failsValidation() {
        // Arrange
        LoginUserDTO dto = new LoginUserDTO();
        dto.setUsername("   ");
        dto.setPassword("validPassword123");

        // Act
        Set<ConstraintViolation<LoginUserDTO>> violations = validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
        ConstraintViolation<LoginUserDTO> violation = violations.iterator().next();
        assertEquals("Username cannot be empty", violation.getMessage());
        assertEquals("username", violation.getPropertyPath().toString());
    }

    @Test
    void validation_withNullPassword_failsValidation() {
        // Arrange
        LoginUserDTO dto = new LoginUserDTO();
        dto.setUsername("validUser");
        dto.setPassword(null);

        // Act
        Set<ConstraintViolation<LoginUserDTO>> violations = validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
        ConstraintViolation<LoginUserDTO> violation = violations.iterator().next();
        assertEquals("Password cannot be empty", violation.getMessage());
        assertEquals("password", violation.getPropertyPath().toString());
    }

    @Test
    void validation_withEmptyPassword_failsValidation() {
        // Arrange
        LoginUserDTO dto = new LoginUserDTO();
        dto.setUsername("validUser");
        dto.setPassword("");

        // Act
        Set<ConstraintViolation<LoginUserDTO>> violations = validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
        ConstraintViolation<LoginUserDTO> violation = violations.iterator().next();
        assertEquals("Password cannot be empty", violation.getMessage());
        assertEquals("password", violation.getPropertyPath().toString());
    }

    @Test
    void validation_withBlankPassword_failsValidation() {
        // Arrange
        LoginUserDTO dto = new LoginUserDTO();
        dto.setUsername("validUser");
        dto.setPassword("   ");

        // Act
        Set<ConstraintViolation<LoginUserDTO>> violations = validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
        ConstraintViolation<LoginUserDTO> violation = violations.iterator().next();
        assertEquals("Password cannot be empty", violation.getMessage());
        assertEquals("password", violation.getPropertyPath().toString());
    }

    @Test
    void validation_withBothFieldsInvalid_failsValidation() {
        // Arrange
        LoginUserDTO dto = new LoginUserDTO();
        dto.setUsername("");
        dto.setPassword("   ");

        // Act
        Set<ConstraintViolation<LoginUserDTO>> violations = validator.validate(dto);

        // Assert
        assertEquals(2, violations.size());
    }

    @Test
    void gettersAndSetters_workCorrectly() {
        // Arrange
        LoginUserDTO dto = new LoginUserDTO();
        String testUsername = "testUser123";
        String testPassword = "securePassword456";

        // Act & Assert
        dto.setUsername(testUsername);
        assertEquals(testUsername, dto.getUsername());

        dto.setPassword(testPassword);
        assertEquals(testPassword, dto.getPassword());
    }
}