package com.beewise.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class OpenExerciseTest {
    @Test
    void openExercise_constructor_setsQuestionAndAnswer() {
        OpenExercise ex = new OpenExercise("What is 2+2?", "4");

        assertEquals("What is 2+2?", ex.getQuestion());
        assertEquals("4", ex.getAnswer());

        // setOptions does nothing
        ex.setOptions(Arrays.asList("X", "Y"));
        assertNull(ex.getLesson()); // Lesson does assign anything
    }
}
