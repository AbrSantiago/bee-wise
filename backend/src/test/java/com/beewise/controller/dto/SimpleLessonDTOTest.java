package com.beewise.controller.dto;

import com.beewise.controller.dto.SimpleLessonDTO;
import com.beewise.model.Lesson;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleLessonDTOTest {
    @Test
    void simpleLessonDTO_constructor_setsFields() {
        Lesson lesson = new Lesson("History", "Ancient");
        lesson.setId(5L);

        SimpleLessonDTO dto = new SimpleLessonDTO(lesson);

        assertEquals(5L, dto.getId());
        assertEquals("History", dto.getTitle());
        assertEquals("Ancient", dto.getDescription());
    }

    @Test
    void simpleLessonDTO_fromLessonList_returnsList() {
        Lesson l1 = new Lesson("A", "descA");
        Lesson l2 = new Lesson("B", "descB");
        l1.setId(1L);
        l2.setId(2L);

        List<SimpleLessonDTO> dtos = SimpleLessonDTO.fromLessonList(Arrays.asList(l1, l2));

        assertEquals(2, dtos.size());
        assertEquals("A", dtos.get(0).getTitle());
        assertEquals("B", dtos.get(1).getTitle());
    }
}
