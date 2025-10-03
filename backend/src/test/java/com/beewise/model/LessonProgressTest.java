package com.beewise.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LessonProgressTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void noArgsConstructor_setsDefaults() {
        LessonProgress progress = new LessonProgress();

        assertNull(progress.getId());
        assertNull(progress.getUser());
        assertNull(progress.getLesson());
        assertEquals(0, progress.getAttempts());
        assertEquals(LocalDate.now(), progress.getFirstTimeCompletedDate());
    }

    @Test
    void parameterizedConstructor_setsFieldsCorrectly() {
        User user = new User();
        user.setId(1L);
        Lesson lesson = new Lesson();
        lesson.setId(10L);

        LessonProgress progress = new LessonProgress(user, lesson);

        assertEquals(user, progress.getUser());
        assertEquals(lesson, progress.getLesson());
        assertEquals(0, progress.getAttempts());
        assertEquals(LocalDate.now(), progress.getFirstTimeCompletedDate());
    }

    @Test
    void setId_setsValueCorrectly() {
        LessonProgress progress = new LessonProgress();

        progress.setId(100L);

        assertEquals(100L, progress.getId());
    }

    @Test
    void setUser_setsValueCorrectly() {
        LessonProgress progress = new LessonProgress();
        User user = new User();
        user.setId(5L);

        progress.setUser(user);

        assertEquals(user, progress.getUser());
    }

    @Test
    void setLesson_setsValueCorrectly() {
        LessonProgress progress = new LessonProgress();
        Lesson lesson = new Lesson();
        lesson.setId(20L);

        progress.setLesson(lesson);

        assertEquals(lesson, progress.getLesson());
    }

    @Test
    void setAttempts_setsValueCorrectly() {
        LessonProgress progress = new LessonProgress();

        progress.setAttempts(5);

        assertEquals(5, progress.getAttempts());
    }

    @Test
    void setFirstTimeCompletedDate_setsValueCorrectly() {
        LessonProgress progress = new LessonProgress();
        LocalDate testDate = LocalDate.of(2025, 1, 15);

        progress.setFirstTimeCompletedDate(testDate);

        assertEquals(testDate, progress.getFirstTimeCompletedDate());
    }

    @Test
    void validation_withValidData_passesValidation() {
        LessonProgress progress = new LessonProgress();
        progress.setAttempts(3);
        progress.setFirstTimeCompletedDate(LocalDate.now());

        Set<ConstraintViolation<LessonProgress>> violations = validator.validate(progress);

        assertTrue(violations.isEmpty());
    }

    @Test
    void validation_withNegativeAttempts_failsValidation() {
        LessonProgress progress = new LessonProgress();
        progress.setAttempts(-1);

        Set<ConstraintViolation<LessonProgress>> violations = validator.validate(progress);

        assertEquals(1, violations.size());
    }

    @Test
    void validation_withZeroAttempts_passesValidation() {
        LessonProgress progress = new LessonProgress();
        progress.setAttempts(0);

        Set<ConstraintViolation<LessonProgress>> violations = validator.validate(progress);

        assertTrue(violations.isEmpty());
    }

    @Test
    void allGetters_workCorrectly() {
        User user = new User();
        user.setId(15L);
        Lesson lesson = new Lesson();
        lesson.setId(25L);
        LocalDate testDate = LocalDate.of(2025, 2, 10);

        LessonProgress progress = new LessonProgress();
        progress.setId(50L);
        progress.setUser(user);
        progress.setLesson(lesson);
        progress.setAttempts(8);
        progress.setFirstTimeCompletedDate(testDate);

        assertEquals(50L, progress.getId());
        assertEquals(user, progress.getUser());
        assertEquals(lesson, progress.getLesson());
        assertEquals(8, progress.getAttempts());
        assertEquals(testDate, progress.getFirstTimeCompletedDate());
    }

    @Test
    void setUser_withNull_handlesCorrectly() {
        LessonProgress progress = new LessonProgress();

        progress.setUser(null);

        assertNull(progress.getUser());
    }

    @Test
    void setLesson_withNull_handlesCorrectly() {
        LessonProgress progress = new LessonProgress();

        progress.setLesson(null);

        assertNull(progress.getLesson());
    }

    @Test
    void setFirstTimeCompletedDate_withNull_handlesCorrectly() {
        LessonProgress progress = new LessonProgress();

        progress.setFirstTimeCompletedDate(null);

        assertNull(progress.getFirstTimeCompletedDate());
    }

    @Test
    void parameterizedConstructor_withNullValues_handlesCorrectly() {
        LessonProgress progress = new LessonProgress(null, null);

        assertNull(progress.getUser());
        assertNull(progress.getLesson());
        assertEquals(0, progress.getAttempts());
        assertEquals(LocalDate.now(), progress.getFirstTimeCompletedDate());
    }
}