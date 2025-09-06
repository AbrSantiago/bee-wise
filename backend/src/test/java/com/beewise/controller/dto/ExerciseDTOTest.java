package com.beewise.controller.dto;

import com.beewise.controller.dto.ExerciseDTO;
import com.beewise.controller.dto.MultipleChoiceExerciseDTO;
import com.beewise.controller.dto.OpenExerciseDTO;
import com.beewise.model.Exercise;
import com.beewise.model.MultipleChoiceExercise;
import com.beewise.model.OpenExercise;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExerciseDTOTest {
    @Test
    void fromExercise_withOpenExercise_returnsOpenExerciseDTO() {
        OpenExercise open = new OpenExercise("Q?", "A");
        open.setId(1L);

        ExerciseDTO dto = ExerciseDTO.fromExercise(open);

        assertInstanceOf(OpenExerciseDTO.class, dto);
        assertEquals(1L, dto.getId());
        assertEquals("Q?", dto.getQuestion());
        assertEquals("OPEN", dto.getType());
        assertEquals("A", ((OpenExerciseDTO) dto).getAnswer());
    }

    @Test
    void fromExercise_withMultipleChoiceExercise_returnsMultipleChoiceExerciseDTO() {
        MultipleChoiceExercise mc = new MultipleChoiceExercise("Q?", Arrays.asList("A","B"), "A");
        mc.setId(2L);

        ExerciseDTO dto = ExerciseDTO.fromExercise(mc);

        assertInstanceOf(MultipleChoiceExerciseDTO.class, dto);
        assertEquals(2L, dto.getId());
        assertEquals("Q?", dto.getQuestion());
        assertEquals("MULTIPLE_CHOICE", dto.getType());
        assertEquals("A", ((MultipleChoiceExerciseDTO) dto).getAnswer());
        assertEquals(Arrays.asList("A","B"), ((MultipleChoiceExerciseDTO) dto).getOptions());
    }

    @Test
    void fromExercise_unknownType_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            ExerciseDTO.fromExercise(new Exercise() {
                @Override
                public void setOptions(List<String> options) {}
            });
        });
    }
}
