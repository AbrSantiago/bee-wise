package com.beewise.controller.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RegisterUserDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void constructor_createsEmptyObject() {
        RegisterUserDTO dto = new RegisterUserDTO();

        assertNull(dto.getName());
        assertNull(dto.getSurname());
        assertNull(dto.getEmail());
        assertNull(dto.getUsername());
        assertNull(dto.getPassword());
    }

    @Test
    void validation_withValidData_passesValidation() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setName("John");
        dto.setSurname("Doe");
        dto.setEmail("john.doe@example.com");
        dto.setUsername("johndoe");
        dto.setPassword("password123");

        Set<ConstraintViolation<RegisterUserDTO>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void validation_withNullName_failsValidation() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setName(null);
        dto.setSurname("Doe");
        dto.setEmail("john.doe@example.com");
        dto.setUsername("johndoe");
        dto.setPassword("password123");

        Set<ConstraintViolation<RegisterUserDTO>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        ConstraintViolation<RegisterUserDTO> violation = violations.iterator().next();
        assertEquals("Name cannot be empty", violation.getMessage());
        assertEquals("name", violation.getPropertyPath().toString());
    }

    @Test
    void validation_withEmptyName_failsValidation() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setName("");
        dto.setSurname("Doe");
        dto.setEmail("john.doe@example.com");
        dto.setUsername("johndoe");
        dto.setPassword("password123");

        Set<ConstraintViolation<RegisterUserDTO>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("Name cannot be empty", violations.iterator().next().getMessage());
    }

    @Test
    void validation_withTooLongName_failsValidation() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setName("VeryLongName");
        dto.setSurname("Doe");
        dto.setEmail("john.doe@example.com");
        dto.setUsername("johndoe");
        dto.setPassword("password123");

        Set<ConstraintViolation<RegisterUserDTO>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("Name is too long", violations.iterator().next().getMessage());
    }

    @Test
    void validation_withNullSurname_failsValidation() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setName("John");
        dto.setSurname(null);
        dto.setEmail("john.doe@example.com");
        dto.setUsername("johndoe");
        dto.setPassword("password123");

        Set<ConstraintViolation<RegisterUserDTO>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("Surname cannot be empty", violations.iterator().next().getMessage());
    }

    @Test
    void validation_withTooLongSurname_failsValidation() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setName("John");
        dto.setSurname("VeryLongSurname");
        dto.setEmail("john.doe@example.com");
        dto.setUsername("johndoe");
        dto.setPassword("password123");

        Set<ConstraintViolation<RegisterUserDTO>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("Surname is too long", violations.iterator().next().getMessage());
    }

    @Test
    void validation_withNullEmail_failsValidation() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setName("John");
        dto.setSurname("Doe");
        dto.setEmail(null);
        dto.setUsername("johndoe");
        dto.setPassword("password123");

        Set<ConstraintViolation<RegisterUserDTO>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("Email cannot be empty", violations.iterator().next().getMessage());
    }

    @Test
    void validation_withInvalidEmail_failsValidation() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setName("John");
        dto.setSurname("Doe");
        dto.setEmail("invalid-email");
        dto.setUsername("johndoe");
        dto.setPassword("password123");

        Set<ConstraintViolation<RegisterUserDTO>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("Email is not valid", violations.iterator().next().getMessage());
    }

    @Test
    void validation_withTooLongEmail_failsValidation() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setName("John");
        dto.setSurname("Doe");
        dto.setEmail("verylongemailaddressthatexceedsfiftycharacterslimittest@example.com");
        dto.setUsername("johndoe");
        dto.setPassword("password123");

        Set<ConstraintViolation<RegisterUserDTO>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("Email is too long", violations.iterator().next().getMessage());
    }

    @Test
    void validation_withNullUsername_failsValidation() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setName("John");
        dto.setSurname("Doe");
        dto.setEmail("john.doe@example.com");
        dto.setUsername(null);
        dto.setPassword("password123");

        Set<ConstraintViolation<RegisterUserDTO>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("Username cannot be empty", violations.iterator().next().getMessage());
    }

    @Test
    void validation_withTooLongUsername_failsValidation() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setName("John");
        dto.setSurname("Doe");
        dto.setEmail("john.doe@example.com");
        dto.setUsername("verylongusername");
        dto.setPassword("password123");

        Set<ConstraintViolation<RegisterUserDTO>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("Username is too long", violations.iterator().next().getMessage());
    }

    @Test
    void validation_withNullPassword_failsValidation() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setName("John");
        dto.setSurname("Doe");
        dto.setEmail("john.doe@example.com");
        dto.setUsername("johndoe");
        dto.setPassword(null);

        Set<ConstraintViolation<RegisterUserDTO>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("Password cannot be empty", violations.iterator().next().getMessage());
    }

    @Test
    void validation_withTooShortPassword_failsValidation() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setName("John");
        dto.setSurname("Doe");
        dto.setEmail("john.doe@example.com");
        dto.setUsername("johndoe");
        dto.setPassword("123");

        Set<ConstraintViolation<RegisterUserDTO>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("Password must be between 6 and 100 characters", violations.iterator().next().getMessage());
    }

    @Test
    void validation_withTooLongPassword_failsValidation() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setName("John");
        dto.setSurname("Doe");
        dto.setEmail("john.doe@example.com");
        dto.setUsername("johndoe");
        dto.setPassword("a".repeat(101));

        Set<ConstraintViolation<RegisterUserDTO>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("Password must be between 6 and 100 characters", violations.iterator().next().getMessage());
    }

    @Test
    void validation_withMultipleErrors_failsValidation() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setName("");
        dto.setSurname("");
        dto.setEmail("invalid");
        dto.setUsername("");
        dto.setPassword("12");

        Set<ConstraintViolation<RegisterUserDTO>> violations = validator.validate(dto);

        assertEquals(5, violations.size());
    }

    @Test
    void gettersAndSetters_workCorrectly() {
        RegisterUserDTO dto = new RegisterUserDTO();

        dto.setName("John");
        assertEquals("John", dto.getName());

        dto.setSurname("Doe");
        assertEquals("Doe", dto.getSurname());

        dto.setEmail("john@example.com");
        assertEquals("john@example.com", dto.getEmail());

        dto.setUsername("johndoe");
        assertEquals("johndoe", dto.getUsername());

        dto.setPassword("password123");
        assertEquals("password123", dto.getPassword());
    }

    @Test
    void validation_withMaxValidLengths_passesValidation() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setName("1234567890");
        dto.setSurname("1234567890");
        dto.setEmail("test@example.com");
        dto.setUsername("1234567890");
        dto.setPassword("123456");

        Set<ConstraintViolation<RegisterUserDTO>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }
}