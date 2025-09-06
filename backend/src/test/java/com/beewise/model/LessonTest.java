package com.beewise.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class LessonTest {
    @Test
    void lesson_constructorWithTitleAndDescription_setsFields() {
        Lesson lesson = new Lesson("Math", "Algebra basics");

        assertEquals("Math", lesson.getTitle());
        assertEquals("Algebra basics", lesson.getDescription());
        assertNotNull(lesson.getExercises());
        assertTrue(lesson.getExercises().isEmpty());
    }

    @Test
    void lesson_constructorWithExercises_setsAllFields() {
        OpenExercise ex1 = new OpenExercise("Q1?", "A1");
        MultipleChoiceExercise ex2 = new MultipleChoiceExercise("Q2?", Arrays.asList("X","Y"), "X");

        Lesson lesson = new Lesson("Science", "Physics", new ArrayList<>(Arrays.asList(ex1, ex2)));

        assertEquals(2, lesson.getExercises().size());
        assertEquals(ex1, lesson.getExercises().get(0));
        assertEquals(ex2, lesson.getExercises().get(1));
    }

    @Test
    void lesson_addExercise_toList() {
        Lesson lesson = new Lesson("Chemistry", "Basics");
        OpenExercise ex = new OpenExercise("Q?", "A");

        lesson.getExercises().add(ex);

        assertEquals(1, lesson.getExercises().size());
        assertEquals(ex, lesson.getExercises().get(0));
    }
}
