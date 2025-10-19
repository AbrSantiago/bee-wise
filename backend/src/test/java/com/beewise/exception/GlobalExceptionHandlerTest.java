package com.beewise.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleLessonNotFoundException_returnsNotFound() {
        LessonNotFoundException ex = new LessonNotFoundException("Lesson missing");
        ResponseEntity<String> response = handler.handleLessonNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Lesson missing", response.getBody());
    }

    @Test
    void handleInvalidIdException_returnsBadRequest() {
        InvalidIdException ex = new InvalidIdException("Invalid ID");
        ResponseEntity<Map<String, String>> response = handler.handleBadRequestExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> body = response.getBody();
        assertNotNull(body);
        assertEquals("Invalid ID", body.get("error"));
    }

    @Test
    void handleLessonAlreadyExistsException_returnsConflict() {
        LessonAlreadyExistsException ex = new LessonAlreadyExistsException("Already exists");
        ResponseEntity<String> response = handler.handleLessonAlreadyExistsException(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Already exists", response.getBody());
    }

    @Test
    void handleExerciseNotFoundException_returnsNotFound() {
        ExerciseNotFoundException ex = new ExerciseNotFoundException("Exercise missing");
        ResponseEntity<String> response = handler.handleExerciseNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Exercise missing", response.getBody());
    }

    @Test
    void handleIllegalArgument_returnsBadRequestWithError() {
        IllegalArgumentException ex = new IllegalArgumentException("Bad arg");
        ResponseEntity<Map<String, String>> response = handler.handleBadRequestExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> body = response.getBody();
        assertNotNull(body);
        assertEquals("Bad arg", body.get("error"));
    }
}
