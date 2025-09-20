package com.beewise.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class MultipleChoiceExerciseTest {
    @Test
    void multipleChoiceExercise_constructor_setsFields() {
        MultipleChoiceExercise ex = new MultipleChoiceExercise("Pick one", Arrays.asList("A", "B", "C"), "B");

        assertEquals("Pick one", ex.getQuestion());
        assertEquals("B", ex.getAnswer());
        assertEquals(Arrays.asList("A", "B", "C"), ex.getOptions());
    }

    @Test
    void multipleChoiceExercise_setOptions_updatesList() {
        MultipleChoiceExercise ex = new MultipleChoiceExercise();
        ex.setOptions(Arrays.asList("Yes", "No"));
        assertEquals(Arrays.asList("Yes", "No"), ex.getOptions());
    }

    @Test
    void multipleChoiceExercise_lessonAssociation() {
        Lesson lesson = new Lesson("History", "Ancient");
        MultipleChoiceExercise ex = new MultipleChoiceExercise("Q?", Arrays.asList("X","Y"), "X");

        ex.setLesson(lesson);

        assertEquals(lesson, ex.getLesson());
    }
}
