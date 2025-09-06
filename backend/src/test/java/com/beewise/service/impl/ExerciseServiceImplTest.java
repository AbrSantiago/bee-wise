package com.beewise.service.impl;

import com.beewise.controller.dto.SimpleMultipleChoiceExerciseDTO;
import com.beewise.controller.dto.SimpleOpenExerciseDTO;
import com.beewise.exception.ExerciseNotFoundException;
import com.beewise.exception.InvalidIdException;
import com.beewise.model.Exercise;
import com.beewise.model.MultipleChoiceExercise;
import com.beewise.model.OpenExercise;
import com.beewise.repository.ExerciseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExerciseServiceImplTest {

    @Mock
    private ExerciseRepository repository;

    @InjectMocks
    private ExerciseServiceImpl service;

    // ---- getExercise ----
    @Test
    void getExercise_validId_returnsExercise() {
        Exercise exercise = new OpenExercise("Q?", "A");
        exercise.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(exercise));

        Exercise result = service.getExercise(1L);

        assertNotNull(result);
        assertEquals("Q?", result.getQuestion());
        verify(repository).findById(1L);
    }

    @Test
    void getExercise_invalidId_throwsException() {
        assertThrows(InvalidIdException.class, () -> service.getExercise(0L));
        assertThrows(InvalidIdException.class, () -> service.getExercise(null));
    }

    @Test
    void getExercise_notFound_throwsException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ExerciseNotFoundException.class, () -> service.getExercise(99L));
    }

    // ---- createOpenExercise ----
    @Test
    void createOpenExercise_savesAndReturns() {
        SimpleOpenExerciseDTO dto = new SimpleOpenExerciseDTO();
        dto.setQuestion("Q?");
        dto.setAnswer("A");

        Exercise saved = new OpenExercise("Q?", "A");
        saved.setId(1L);

        when(repository.save(any(Exercise.class))).thenReturn(saved);

        Exercise result = service.createOpenExercise(dto);

        assertEquals(1L, result.getId());
        assertEquals("A", result.getAnswer());
        verify(repository).save(any(Exercise.class));
    }


    // ---- createMultipleChoiceExercise ----
    @Test
    void createMultipleChoiceExercise_savesAndReturns() {
        List<String> options = Arrays.asList("A", "B", "C");

        SimpleMultipleChoiceExerciseDTO dto = new SimpleMultipleChoiceExerciseDTO();
        dto.setQuestion("Q?");
        dto.setOptions(options);
        dto.setAnswer("A");

        Exercise saved = new MultipleChoiceExercise("Q?", options, "A");
        saved.setId(2L);

        when(repository.save(any(Exercise.class))).thenReturn(saved);

        Exercise result = service.createMultipleChoiceExercise(dto);

        assertEquals(2L, result.getId());
        assertEquals(options, ((MultipleChoiceExercise) result).getOptions());
        verify(repository).save(any(Exercise.class));
    }


    // ---- updateOpenExercise ----
    @Test
    void updateOpenExercise_valid_updatesFields() {
        OpenExercise exercise = new OpenExercise("OldQ", "OldA");
        exercise.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(exercise));
        when(repository.save(exercise)).thenReturn(exercise);

        SimpleOpenExerciseDTO dto = new SimpleOpenExerciseDTO();
        dto.setQuestion("NewQ");
        dto.setAnswer("NewA");
        Exercise result = service.updateOpenExercise(1L, dto);

        assertEquals("NewQ", result.getQuestion());
        assertEquals("NewA", result.getAnswer());
    }

    @Test
    void updateOpenExercise_wrongType_throwsException() {
        MultipleChoiceExercise mc = new MultipleChoiceExercise("Q", List.of("A"), "A");
        mc.setId(5L);

        when(repository.findById(5L)).thenReturn(Optional.of(mc));

        SimpleOpenExerciseDTO dto2 = new SimpleOpenExerciseDTO();
        dto2.setQuestion("Q");
        dto2.setAnswer("A");
        assertThrows(InvalidIdException.class,
                () -> service.updateOpenExercise(5L, dto2));
    }

    // ---- updateMultipleChoiceExercise ----
    @Test
    void updateMultipleChoiceExercise_valid_updatesFields() {
        MultipleChoiceExercise mc = new MultipleChoiceExercise("OldQ", List.of("A", "B"), "A");
        mc.setId(2L);

        when(repository.findById(2L)).thenReturn(Optional.of(mc));
        when(repository.save(mc)).thenReturn(mc);

        SimpleMultipleChoiceExerciseDTO mcDto = new SimpleMultipleChoiceExerciseDTO();
        mcDto.setQuestion("NewQ");
        mcDto.setOptions(List.of("X", "Y"));
        mcDto.setAnswer("Y");
        Exercise result = service.updateMultipleChoiceExercise(2L, mcDto);


        assertEquals("NewQ", result.getQuestion());
        assertEquals(List.of("X", "Y"), ((MultipleChoiceExercise) result).getOptions());
        assertEquals("Y", result.getAnswer());
    }

    @Test
    void updateMultipleChoiceExercise_wrongType_throwsException() {
        OpenExercise open = new OpenExercise("Q", "A");
        open.setId(10L);

        when(repository.findById(10L)).thenReturn(Optional.of(open));

        SimpleMultipleChoiceExerciseDTO mcDto2 = new SimpleMultipleChoiceExerciseDTO();
        mcDto2.setQuestion("Q");
        mcDto2.setOptions(List.of("A"));
        mcDto2.setAnswer("A");
        assertThrows(InvalidIdException.class,
                () -> service.updateMultipleChoiceExercise(10L, mcDto2));
    }

    // ---- deleteExercise ----
    @Test
    void deleteExercise_callsRepository() {
        service.deleteExercise(1L);
        verify(repository).deleteById(1L);
    }
}