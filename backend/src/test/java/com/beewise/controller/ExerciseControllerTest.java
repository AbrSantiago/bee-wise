package com.beewise.controller;

import com.beewise.controller.dto.ExerciseDTO;
import com.beewise.controller.dto.SimpleMultipleChoiceExerciseDTO;
import com.beewise.controller.dto.SimpleOpenExerciseDTO;
import com.beewise.model.Exercise;
import com.beewise.model.ExerciseCategory;
import com.beewise.model.MultipleChoiceExercise;
import com.beewise.model.OpenExercise;
import com.beewise.service.ExerciseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExerciseControllerTest {

    @Mock
    private ExerciseService service;

    private ExerciseController controller;

    @BeforeEach
    void setUp() {
        controller = new ExerciseController(service);
    }

    @Test
    void getExercise_returnsDto() {
        OpenExercise ex = new OpenExercise("Q?", "A", ExerciseCategory.MATRICES);
        ex.setId(1L);

        when(service.getExercise(1L)).thenReturn(ex);

        ResponseEntity<?> response = controller.getExercise(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Q?", ((com.beewise.controller.dto.ExerciseDTO) response.getBody()).getQuestion());
        verify(service).getExercise(1L);
    }

    @Test
    void createOpenExercise_returnsDto() {
        SimpleOpenExerciseDTO dto = new SimpleOpenExerciseDTO();
        dto.setQuestion("Q?");
        dto.setAnswer("A");

        OpenExercise ex = new OpenExercise("Q?", "A", ExerciseCategory.MATRICES);
        ex.setId(2L);

        when(service.createOpenExercise(dto)).thenReturn(ex);

        ResponseEntity<?> response = controller.createOpenExercise(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        com.beewise.controller.dto.ExerciseDTO body =
                (com.beewise.controller.dto.ExerciseDTO) response.getBody();
        assertEquals(2L, body.getId());
        verify(service).createOpenExercise(dto);
    }

    @Test
    void createMultipleChoiceExercise_returnsDto() {
        List<String> options = List.of("A", "B");
        SimpleMultipleChoiceExerciseDTO dto = new SimpleMultipleChoiceExerciseDTO();
        dto.setQuestion("Q?");
        dto.setAnswer("A");
        dto.setOptions(options);

        MultipleChoiceExercise ex = new MultipleChoiceExercise("Q?", options, "A");
        ex.setId(3L);

        when(service.createMultipleChoiceExercise(dto)).thenReturn(ex);

        ResponseEntity<?> response = controller.createMultipleChoiceExercise(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        com.beewise.controller.dto.ExerciseDTO body =
                (com.beewise.controller.dto.ExerciseDTO) response.getBody();
        assertEquals(3L, body.getId());
        verify(service).createMultipleChoiceExercise(dto);
    }

    @Test
    void updateOpenExercise_returnsDto() {
        SimpleOpenExerciseDTO dto = new SimpleOpenExerciseDTO();
        dto.setQuestion("Q2");
        dto.setAnswer("B");

        OpenExercise ex = new OpenExercise("Q2", "B", ExerciseCategory.MATRICES);
        ex.setId(4L);

        when(service.updateOpenExercise(4L, dto)).thenReturn(ex);

        ResponseEntity<?> response = controller.updateOpenExercise(4L, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        com.beewise.controller.dto.ExerciseDTO body =
                (com.beewise.controller.dto.ExerciseDTO) response.getBody();
        assertEquals("Q2", body.getQuestion());
        verify(service).updateOpenExercise(4L, dto);
    }

    @Test
    void updateMultipleChoiceExercise_returnsDto() {
        List<String> options = List.of("X", "Y");
        SimpleMultipleChoiceExerciseDTO dto = new SimpleMultipleChoiceExerciseDTO();
        dto.setQuestion("Q2");
        dto.setAnswer("Y");
        dto.setOptions(options);

        MultipleChoiceExercise ex = new MultipleChoiceExercise("Q2", options, "Y");
        ex.setId(5L);

        when(service.updateMultipleChoiceExercise(5L, dto)).thenReturn(ex);

        ResponseEntity<?> response = controller.updateMultipleChoiceExercise(5L, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        com.beewise.controller.dto.ExerciseDTO body =
                (com.beewise.controller.dto.ExerciseDTO) response.getBody();
        assertEquals("Q2", body.getQuestion());
        verify(service).updateMultipleChoiceExercise(5L, dto);
    }

    @Test
    void deleteExercise_returnsNoContent() {
        doNothing().when(service).deleteExercise(6L);

        ResponseEntity<Void> response = controller.deleteExercise(6L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service).deleteExercise(6L);
    }

    @Test
    void createAll_returnsListOfDtos() {
        SimpleOpenExerciseDTO dto1 = new SimpleOpenExerciseDTO();
        dto1.setQuestion("Q1?");
        dto1.setAnswer("A1");

        SimpleOpenExerciseDTO dto2 = new SimpleOpenExerciseDTO();
        dto2.setQuestion("Q2?");
        dto2.setAnswer("A2");

        OpenExercise ex1 = new OpenExercise("Q1?", "A1", ExerciseCategory.MATRICES);
        ex1.setId(10L);

        OpenExercise ex2 = new OpenExercise("Q2?", "A2", ExerciseCategory.DETERMINANTS);
        ex2.setId(11L);

        List<SimpleOpenExerciseDTO> dtos = List.of(dto1, dto2);
        List<Exercise> exercises = List.of(ex1, ex2);

        when(service.createOpenExercises(dtos)).thenReturn(exercises);

        ResponseEntity<List<ExerciseDTO>> response = controller.createAll(dtos);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Q1?", response.getBody().get(0).getQuestion());
        assertEquals("Q2?", response.getBody().get(1).getQuestion());
        verify(service).createOpenExercises(dtos);
    }
}
