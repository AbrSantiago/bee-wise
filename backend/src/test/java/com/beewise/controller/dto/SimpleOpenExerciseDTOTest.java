package com.beewise.controller.dto;

import com.beewise.controller.dto.SimpleOpenExerciseDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleOpenExerciseDTOTest {
    @Test
    void simpleOpenExerciseDTO_setters_getters_work() {
        SimpleOpenExerciseDTO dto = new SimpleOpenExerciseDTO();
        dto.setQuestion("Q?");
        dto.setAnswer("A");

        assertEquals("Q?", dto.getQuestion());
        assertEquals("A", dto.getAnswer());
    }
}
