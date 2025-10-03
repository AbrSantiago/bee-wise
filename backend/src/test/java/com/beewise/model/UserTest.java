package com.beewise.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void noArgsConstructor_createsObjectWithDefaults() {
        User user = new User();

        assertNull(user.getId());
        assertNull(user.getName());
        assertNull(user.getSurname());
        assertNull(user.getEmail());
        assertNull(user.getUsername());
        assertNull(user.getPasswordHash());
        assertEquals(0, user.getPoints());
        assertEquals(1, user.getCurrentLesson());
        assertNotNull(user.getLessonProgresses());
        assertTrue(user.getLessonProgresses().isEmpty());
    }

    @Test
    void parameterizedConstructor_setsAllFields() {
        List<LessonProgress> progresses = new ArrayList<>();

        User user = new User("John", "Doe", "john@example.com", "johndoe", "hashedPassword", 150, progresses);

        assertEquals("John", user.getName());
        assertEquals("Doe", user.getSurname());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("johndoe", user.getUsername());
        assertEquals("hashedPassword", user.getPasswordHash());
        assertEquals(150, user.getPoints());
        assertEquals(progresses, user.getLessonProgresses());
        assertEquals(1, user.getCurrentLesson());
    }

    @Test
    void setId_setsValueCorrectly() {
        User user = new User();

        user.setId(100L);

        assertEquals(100L, user.getId());
    }

    @Test
    void setName_setsValueCorrectly() {
        User user = new User();

        user.setName("Alice");

        assertEquals("Alice", user.getName());
    }

    @Test
    void setSurname_setsValueCorrectly() {
        User user = new User();

        user.setSurname("Smith");

        assertEquals("Smith", user.getSurname());
    }

    @Test
    void setEmail_setsValueCorrectly() {
        User user = new User();

        user.setEmail("alice@example.com");

        assertEquals("alice@example.com", user.getEmail());
    }

    @Test
    void setUsername_setsValueCorrectly() {
        User user = new User();

        user.setUsername("alicesmith");

        assertEquals("alicesmith", user.getUsername());
    }

    @Test
    void setPasswordHash_setsValueCorrectly() {
        User user = new User();

        user.setPasswordHash("newHashedPassword");

        assertEquals("newHashedPassword", user.getPasswordHash());
    }

    @Test
    void setPoints_setsValueCorrectly() {
        User user = new User();

        user.setPoints(250);

        assertEquals(250, user.getPoints());
    }

    @Test
    void setCurrentLesson_setsValueCorrectly() {
        User user = new User();

        user.setCurrentLesson(5);

        assertEquals(5, user.getCurrentLesson());
    }

    @Test
    void setLessonProgresses_setsValueCorrectly() {
        User user = new User();
        List<LessonProgress> progresses = new ArrayList<>();
        LessonProgress progress = new LessonProgress();
        progresses.add(progress);

        user.setLessonProgresses(progresses);

        assertEquals(progresses, user.getLessonProgresses());
        assertEquals(1, user.getLessonProgresses().size());
    }

    @Test
    void validation_withValidData_passesValidation() {
        User user = new User();
        user.setName("John");
        user.setSurname("Doe");
        user.setEmail("john.doe@example.com");
        user.setUsername("johndoe");
        user.setPasswordHash("hashedPassword");
        user.setPoints(100);
        user.setCurrentLesson(3);

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty());
    }

    @Test
    void validation_withNullName_failsValidation() {
        User user = createValidUser();
        user.setName(null);

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());
        assertEquals("Name cannot be empty", violations.iterator().next().getMessage());
    }

    @Test
    void validation_withEmptyName_failsValidation() {
        User user = createValidUser();
        user.setName("");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());
        assertEquals("Name cannot be empty", violations.iterator().next().getMessage());
    }

    @Test
    void validation_withTooLongName_failsValidation() {
        User user = createValidUser();
        user.setName("a".repeat(51));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());
        assertEquals("Name is too long", violations.iterator().next().getMessage());
    }

    @Test
    void validation_withNullSurname_failsValidation() {
        User user = createValidUser();
        user.setSurname(null);

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());
        assertEquals("Surname cannot be empty", violations.iterator().next().getMessage());
    }

    @Test
    void validation_withTooLongSurname_failsValidation() {
        User user = createValidUser();
        user.setSurname("b".repeat(51));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());
        assertEquals("Surname is too long", violations.iterator().next().getMessage());
    }

    @Test
    void validation_withInvalidEmail_failsValidation() {
        User user = createValidUser();
        user.setEmail("invalid-email");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());
        assertEquals("Email is not valid", violations.iterator().next().getMessage());
    }

    @Test
    void validation_withInvalidAndLongEmail_failsMultipleValidations() {
        User user = createValidUser();
        user.setEmail("a".repeat(95) + "@example.com"); // Inválido Y largo

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(2, violations.size()); // Falla tanto @Email como @Size
    }

    @Test
    void validation_withTooLongUsername_failsValidation() {
        User user = createValidUser();
        user.setUsername("a".repeat(31));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());
        assertEquals("Username is too long", violations.iterator().next().getMessage());
    }

    @Test
    void validation_withNegativePoints_failsValidation() {
        User user = createValidUser();
        user.setPoints(-10);

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());
        assertEquals("Points cannot be negative", violations.iterator().next().getMessage());
    }

    @Test
    void validation_withZeroCurrentLesson_failsValidation() {
        User user = createValidUser();
        user.setCurrentLesson(0); // 0 es menor que el mínimo de 1

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());
        assertEquals("Current lesson cannot be negative", violations.iterator().next().getMessage());
    }

    @Test
    void validation_withNullEmail_failsValidation() {
        User user = createValidUser();
        user.setEmail(null);

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());
        assertEquals("Email cannot be empty", violations.iterator().next().getMessage());
    }

    @Test
    void validation_withEmptyEmail_failsValidation() {
        User user = createValidUser();
        user.setEmail("");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());
        assertEquals("Email cannot be empty", violations.iterator().next().getMessage());
    }

    @Test
    void validation_withNullUsername_failsValidation() {
        User user = createValidUser();
        user.setUsername(null);

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());
        assertEquals("Username cannot be empty", violations.iterator().next().getMessage());
    }

    @Test
    void validation_withEmptyUsername_failsValidation() {
        User user = createValidUser();
        user.setUsername("");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());
        assertEquals("Username cannot be empty", violations.iterator().next().getMessage());
    }

    @Test
    void validation_withNegativeCurrentLesson_failsValidation() {
        User user = createValidUser();
        user.setCurrentLesson(-1);

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());
        assertEquals("Current lesson cannot be negative", violations.iterator().next().getMessage());
    }

    @Test
    void allGetters_workCorrectly() {
        User user = new User();
        user.setId(50L);
        user.setName("Bob");
        user.setSurname("Wilson");
        user.setEmail("bob@example.com");
        user.setUsername("bobwilson");
        user.setPasswordHash("bobHash");
        user.setPoints(300);
        user.setCurrentLesson(7);
        List<LessonProgress> progresses = new ArrayList<>();
        user.setLessonProgresses(progresses);

        assertEquals(50L, user.getId());
        assertEquals("Bob", user.getName());
        assertEquals("Wilson", user.getSurname());
        assertEquals("bob@example.com", user.getEmail());
        assertEquals("bobwilson", user.getUsername());
        assertEquals("bobHash", user.getPasswordHash());
        assertEquals(300, user.getPoints());
        assertEquals(7, user.getCurrentLesson());
        assertEquals(progresses, user.getLessonProgresses());
    }

    private User createValidUser() {
        User user = new User();
        user.setName("John");
        user.setSurname("Doe");
        user.setEmail("john@example.com");
        user.setUsername("johndoe");
        user.setPasswordHash("hashedPassword");
        user.setPoints(100);
        user.setCurrentLesson(2);
        return user;
    }
}