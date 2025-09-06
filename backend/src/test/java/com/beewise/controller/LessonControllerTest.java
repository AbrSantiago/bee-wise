package com.beewise.controller;

import com.beewise.controller.dto.LessonDTO;
import com.beewise.controller.dto.SimpleLessonDTO;
import com.beewise.model.Lesson;
import com.beewise.service.LessonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonControllerTest {

    @Mock
    private LessonService lessonService;

    private LessonController controller;

    @BeforeEach
    void setUp() {
        controller = new LessonController(lessonService);
    }

    @Test
    void getLesson_returnsDto() {
        Lesson lesson = new Lesson("Title1", "Desc1");
        lesson.setId(1L);
        when(lessonService.getLessonById(1L)).thenReturn(lesson);

        ResponseEntity<LessonDTO> response = controller.getLesson(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        LessonDTO body = Objects.requireNonNull(response.getBody());
        assertEquals(1L, body.getId());
        assertEquals("Title1", body.getTitle());
        verify(lessonService).getLessonById(1L);
    }

    @Test
    void getAllLessons_returnsList() {
        Lesson lesson1 = new Lesson("T1", "D1"); lesson1.setId(1L);
        Lesson lesson2 = new Lesson("T2", "D2"); lesson2.setId(2L);

        when(lessonService.getAllLessons()).thenReturn(List.of(lesson1, lesson2));

        ResponseEntity<List<SimpleLessonDTO>> response = controller.getAllLessons();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<SimpleLessonDTO> body = Objects.requireNonNull(response.getBody());
        assertEquals(2, body.size());
        assertEquals("T1", body.get(0).getTitle());
        assertEquals("T2", body.get(1).getTitle());
        verify(lessonService).getAllLessons();
    }

    @Test
    void createLesson_returnsDto() {
        SimpleLessonDTO dto = new SimpleLessonDTO();
        dto.setTitle("New Lesson");
        dto.setDescription("Desc");
        Lesson saved = new Lesson("New Lesson", "Desc");
        saved.setId(3L);
        when(lessonService.createLesson(dto)).thenReturn(saved);

        ResponseEntity<LessonDTO> response = controller.createLesson(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        LessonDTO body = Objects.requireNonNull(response.getBody());
        assertEquals(3L, body.getId());
        assertEquals("New Lesson", body.getTitle());
        verify(lessonService).createLesson(dto);
    }

    @Test
    void updateLesson_returnsDto() {
        SimpleLessonDTO dto = new SimpleLessonDTO();
        dto.setTitle("Updated");
        dto.setDescription("Desc2");
        Lesson updated = new Lesson("Updated", "Desc2");
        updated.setId(4L);
        when(lessonService.updateLesson(4L, dto)).thenReturn(updated);

        ResponseEntity<LessonDTO> response = controller.updateLesson(4L, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        LessonDTO body = Objects.requireNonNull(response.getBody());
        assertEquals(4L, body.getId());
        assertEquals("Updated", body.getTitle());
        verify(lessonService).updateLesson(4L, dto);
    }

    @Test
    void addExercise_returnsDto() {
        Lesson lesson = new Lesson("T", "D");
        lesson.setId(5L);
        when(lessonService.addExercise(5L, 10L)).thenReturn(lesson);

        ResponseEntity<LessonDTO> response = controller.addExercise(5L, 10L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        LessonDTO body = Objects.requireNonNull(response.getBody());
        assertEquals(5L, body.getId());
        verify(lessonService).addExercise(5L, 10L);
    }

    @Test
    void removeExercise_returnsDto() {
        Lesson lesson = new Lesson("T", "D");
        lesson.setId(6L);
        when(lessonService.removeExercise(6L, 20L)).thenReturn(lesson);

        ResponseEntity<LessonDTO> response = controller.removeExercise(6L, 20L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        LessonDTO body = Objects.requireNonNull(response.getBody());
        assertEquals(6L, body.getId());
        verify(lessonService).removeExercise(6L, 20L);
    }

    @Test
    void deleteLesson_returnsNoContent() {
        doNothing().when(lessonService).deleteLesson(7L);

        ResponseEntity<Void> response = controller.deleteLesson(7L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(lessonService).deleteLesson(7L);
    }
}
