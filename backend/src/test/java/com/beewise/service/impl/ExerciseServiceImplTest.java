package com.beewise.service.impl;

import com.beewise.controller.dto.SimpleMultipleChoiceExerciseDTO;
import com.beewise.controller.dto.SimpleOpenExerciseDTO;
import com.beewise.exception.ExerciseNotFoundException;
import com.beewise.exception.InvalidIdException;
import com.beewise.model.Exercise;
import com.beewise.model.MultipleChoiceExercise;
import com.beewise.model.OpenExercise;
import com.beewise.repository.ExerciseRepository;
import com.beewise.service.ExerciseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ExerciseServiceImplTest {

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private ExerciseRepository exerciseRepository;

    private Exercise testOpenExercise;
    private Exercise testMultipleChoiceExercise;

    @BeforeEach
    void setUp() {
        testOpenExercise = createTestOpenExercise();
        testMultipleChoiceExercise = createTestMultipleChoiceExercise();
    }

    private Exercise createTestOpenExercise() {
        SimpleOpenExerciseDTO dto = new SimpleOpenExerciseDTO();
        dto.setQuestion("What is 2 + 2?");
        dto.setAnswer("4");
        return exerciseService.createOpenExercise(dto);
    }

    private Exercise createTestMultipleChoiceExercise() {
        SimpleMultipleChoiceExerciseDTO dto = new SimpleMultipleChoiceExerciseDTO();
        dto.setQuestion("What is the capital of France?");
        // Usar ArrayList en lugar de Arrays.asList()
        dto.setOptions(new ArrayList<>(Arrays.asList("London", "Berlin", "Paris", "Madrid")));
        dto.setAnswer("Paris");
        return exerciseService.createMultipleChoiceExercise(dto);
    }

    @Test
    void getExercise_validId_returnsExercise() {
        Exercise result = exerciseService.getExercise(testOpenExercise.getId());

        assertNotNull(result);
        assertEquals(testOpenExercise.getId(), result.getId());
        assertEquals(testOpenExercise.getQuestion(), result.getQuestion());
        assertEquals(testOpenExercise.getAnswer(), result.getAnswer());
    }

    @Test
    void getExercise_invalidId_throwsException() {
        InvalidIdException exception = assertThrows(
                InvalidIdException.class,
                () -> exerciseService.getExercise(-1L)
        );

        assertEquals("Exercise id must be a positive number", exception.getMessage());
    }

    @Test
    void getExercise_notFound_throwsException() {
        Long nonExistentId = 999999L;

        ExerciseNotFoundException exception = assertThrows(
                ExerciseNotFoundException.class,
                () -> exerciseService.getExercise(nonExistentId)
        );

        assertEquals("Exercise with id " + nonExistentId + " not found", exception.getMessage());
    }

    @Test
    void createOpenExercise_validData_savesAndReturns() {
        SimpleOpenExerciseDTO dto = new SimpleOpenExerciseDTO();
        dto.setQuestion("What is 5 + 3?");
        dto.setAnswer("8");

        Exercise result = exerciseService.createOpenExercise(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("What is 5 + 3?", result.getQuestion());
        assertEquals("8", result.getAnswer());
        assertTrue(result instanceof OpenExercise);
    }

    @Test
    void createMultipleChoiceExercise_validData_savesAndReturns() {
        SimpleMultipleChoiceExerciseDTO dto = new SimpleMultipleChoiceExerciseDTO();
        dto.setQuestion("What is the largest planet?");
        // Usar ArrayList en lugar de Arrays.asList()
        dto.setOptions(new ArrayList<>(Arrays.asList("Earth", "Jupiter", "Mars", "Venus")));
        dto.setAnswer("Jupiter");

        Exercise result = exerciseService.createMultipleChoiceExercise(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("What is the largest planet?", result.getQuestion());
        assertEquals("Jupiter", result.getAnswer());
        assertTrue(result instanceof MultipleChoiceExercise);

        MultipleChoiceExercise mcExercise = (MultipleChoiceExercise) result;
        assertEquals(4, mcExercise.getOptions().size());
        assertTrue(mcExercise.getOptions().contains("Jupiter"));
    }

    @Test
    void updateOpenExercise_validData_updatesFields() {
        SimpleOpenExerciseDTO dto = new SimpleOpenExerciseDTO();
        dto.setQuestion("Updated question");
        dto.setAnswer("Updated answer");

        Exercise result = exerciseService.updateOpenExercise(testOpenExercise.getId(), dto);

        assertNotNull(result);
        assertEquals(testOpenExercise.getId(), result.getId());
        assertEquals("Updated question", result.getQuestion());
        assertEquals("Updated answer", result.getAnswer());
    }

    @Test
    void updateOpenExercise_wrongType_throwsException() {
        SimpleOpenExerciseDTO dto = new SimpleOpenExerciseDTO();
        dto.setQuestion("Updated question");
        dto.setAnswer("Updated answer");

        InvalidIdException exception = assertThrows(
                InvalidIdException.class,
                () -> exerciseService.updateOpenExercise(testMultipleChoiceExercise.getId(), dto)
        );

        assertEquals("Exercise with id " + testMultipleChoiceExercise.getId() + " is not Open",
                exception.getMessage());
    }

    @Test
    void updateMultipleChoiceExercise_validData_updatesFields() {
        SimpleMultipleChoiceExerciseDTO dto = new SimpleMultipleChoiceExerciseDTO();
        dto.setQuestion("Updated MC question");
        // Usar ArrayList en lugar de Arrays.asList()
        dto.setOptions(new ArrayList<>(Arrays.asList("A", "B", "C", "D")));
        dto.setAnswer("C");

        Exercise result = exerciseService.updateMultipleChoiceExercise(testMultipleChoiceExercise.getId(), dto);

        assertNotNull(result);
        assertEquals(testMultipleChoiceExercise.getId(), result.getId());
        assertEquals("Updated MC question", result.getQuestion());
        assertEquals("C", result.getAnswer());

        MultipleChoiceExercise mcExercise = (MultipleChoiceExercise) result;
        assertEquals(4, mcExercise.getOptions().size());
        assertTrue(mcExercise.getOptions().contains("C"));
    }

    @Test
    void deleteExercise_validId_deletesExercise() {
        Long exerciseId = testOpenExercise.getId();

        exerciseService.deleteExercise(exerciseId);

        assertFalse(exerciseRepository.existsById(exerciseId));
    }

    @Test
    void contextLoads() {
        assertNotNull(exerciseService);
        assertNotNull(exerciseRepository);
    }
}