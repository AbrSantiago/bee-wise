package com.beewise.controller.dto;

import com.beewise.controller.dto.LessonDTO;
import com.beewise.controller.dto.OpenExerciseDTO;
import com.beewise.model.ExerciseCategory;
import com.beewise.model.Lesson;
import com.beewise.model.OpenExercise;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class LessonDTOTest {
    @Test
    void lessonDTO_constructor_setsFields() {
        Lesson lesson = new Lesson("Math", "Algebra");
        lesson.setId(1L);
        OpenExercise ex = new OpenExercise("Q1?", "A1", ExerciseCategory.MATRICES);
        ex.setId(10L);
        lesson.getExercises().add(ex);

        LessonDTO dto = new LessonDTO(lesson);

        assertEquals(1L, dto.getId());
        assertEquals("Math", dto.getTitle());
        assertEquals("Algebra", dto.getDescription());
        assertEquals(1, dto.getExercises().size());
        assertInstanceOf(OpenExerciseDTO.class, dto.getExercises().get(0));
    }

    @Test
    void lessonDTO_fromLesson_staticMethod_returnsDto() {
        Lesson lesson = new Lesson("Science", "Physics");
        LessonDTO dto = LessonDTO.fromLesson(lesson);

        assertEquals("Science", dto.getTitle());
        assertEquals("Physics", dto.getDescription());
    }
}
