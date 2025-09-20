package com.beewise.service.impl;

import com.beewise.controller.dto.SimpleLessonDTO;
import com.beewise.exception.InvalidIdException;
import com.beewise.exception.LessonNotFoundException;
import com.beewise.model.Exercise;
import com.beewise.model.Lesson;
import com.beewise.model.MultipleChoiceExercise;
import com.beewise.repository.ExerciseRepository;
import com.beewise.repository.LessonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonServiceImplTest {

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private ExerciseRepository exerciseRepository;

    @InjectMocks
    private LessonServiceImpl service;

    @Test
    void getLessonById_validId_returnsLesson() {
        Lesson lesson = new Lesson("Title", "Desc");
        lesson.setId(1L);

        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));

        Lesson result = service.getLessonById(1L);

        assertNotNull(result);
        assertEquals("Title", result.getTitle());
        verify(lessonRepository).findById(1L);
    }

    @Test
    void getLessonById_invalidId_throwsException() {
        assertThrows(InvalidIdException.class, () -> service.getLessonById(0L));
        assertThrows(InvalidIdException.class, () -> service.getLessonById(null));
    }

    @Test
    void getLessonById_notFound_throwsException() {
        when(lessonRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(LessonNotFoundException.class, () -> service.getLessonById(99L));
    }

    @Test
    void createLesson_savesAndReturnsLesson() {
        Lesson lesson = new Lesson("Title", "Desc");
        SimpleLessonDTO dto = new SimpleLessonDTO(lesson);
        Lesson savedLesson = new Lesson("Title", "Desc");
        savedLesson.setId(1L);

        when(lessonRepository.save(any(Lesson.class))).thenReturn(savedLesson);

        Lesson result = service.createLesson(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(lessonRepository).save(any(Lesson.class));
    }

    @Test
    void addExercise_validIds_addsExercise() {
        Lesson lesson = new Lesson("Title", "Desc");
        lesson.setId(1L);
        lesson.setExercises(new ArrayList<>());

        Exercise exercise = new MultipleChoiceExercise();
        exercise.setId(2L);

        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        when(exerciseRepository.findById(2L)).thenReturn(Optional.of(exercise));
        when(lessonRepository.save(any(Lesson.class))).thenReturn(lesson);
        when(exerciseRepository.save(any(Exercise.class))).thenReturn(exercise);

        Lesson result = service.addExercise(1L, 2L);

        assertTrue(result.getExercises().contains(exercise));
        assertEquals(lesson, exercise.getLesson());
    }

    @Test
    void addExercise_alreadyExists_throwsException() {
        Lesson lesson = new Lesson("Title", "Desc");
        lesson.setId(1L);

        Exercise exercise = new MultipleChoiceExercise();
        exercise.setId(2L);

        lesson.setExercises(new ArrayList<>(List.of(exercise)));

        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        when(exerciseRepository.findById(2L)).thenReturn(Optional.of(exercise));

        assertThrows(InvalidIdException.class, () -> service.addExercise(1L, 2L));
    }

    @Test
    void removeExercise_valid_removesExercise() {
        Lesson lesson = new Lesson("Title", "Desc");
        lesson.setId(1L);

        Exercise exercise = new MultipleChoiceExercise();
        exercise.setId(2L);
        exercise.setLesson(lesson);

        lesson.setExercises(new ArrayList<>(List.of(exercise)));

        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        when(exerciseRepository.findById(2L)).thenReturn(Optional.of(exercise));
        when(lessonRepository.save(any(Lesson.class))).thenReturn(lesson);

        Lesson result = service.removeExercise(1L, 2L);

        assertFalse(result.getExercises().contains(exercise));
        assertNull(exercise.getLesson());
    }

    @Test
    void getAllLessons_returnsList() {
        List<Lesson> lessons = List.of(new Lesson("A", "descA"), new Lesson("B", "descB"));
        when(lessonRepository.findAll()).thenReturn(lessons);

        List<Lesson> result = service.getAllLessons();

        assertEquals(2, result.size());
        verify(lessonRepository).findAll();
    }

    @Test
    void updateLesson_existingLesson_updatesFields() {
        Lesson lesson = new Lesson("Old", "OldDesc");
        lesson.setId(1L);

        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        when(lessonRepository.save(lesson)).thenReturn(lesson);

        SimpleLessonDTO dto = new SimpleLessonDTO();
        dto.setTitle("New");
        dto.setDescription("NewDesc");

        Lesson result = service.updateLesson(1L, dto);

        assertEquals("New", result.getTitle());
        assertEquals("NewDesc", result.getDescription());
        verify(lessonRepository).save(lesson);
    }

    @Test
    void updateLesson_notFound_throwsException() {
        when(lessonRepository.findById(99L)).thenReturn(Optional.empty());

        SimpleLessonDTO dto = new SimpleLessonDTO();
        dto.setTitle("X");
        dto.setDescription("Y");

        assertThrows(LessonNotFoundException.class, () -> service.updateLesson(99L, dto));
    }

    // ---- addExercise InvalidIdException ----
    @Test
    void addExercise_invalidLessonId_throwsException() {
        assertThrows(InvalidIdException.class, () -> service.addExercise(0L, 1L));
        assertThrows(InvalidIdException.class, () -> service.addExercise(null, 1L));
    }

    @Test
    void addExercise_invalidExerciseId_throwsException() {
        assertThrows(InvalidIdException.class, () -> service.addExercise(1L, 0L));
        assertThrows(InvalidIdException.class, () -> service.addExercise(1L, null));
    }

    // ---- removeExercise InvalidIdException ----
    @Test
    void removeExercise_invalidLessonId_throwsException() {
        assertThrows(InvalidIdException.class, () -> service.removeExercise(0L, 1L));
        assertThrows(InvalidIdException.class, () -> service.removeExercise(null, 1L));
    }

    @Test
    void removeExercise_invalidExerciseId_throwsException() {
        assertThrows(InvalidIdException.class, () -> service.removeExercise(1L, 0L));
        assertThrows(InvalidIdException.class, () -> service.removeExercise(1L, null));
    }

    @Test
    void removeExercise_exerciseNotInLesson_throwsException() {
        Lesson lesson = new Lesson("L", "Desc");
        lesson.setId(1L);
        Exercise ex = mock(Exercise.class);
        ex.setId(5L);

        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        when(exerciseRepository.findById(5L)).thenReturn(Optional.of(ex));

        assertThrows(InvalidIdException.class, () -> service.removeExercise(1L, 5L));
    }

    @Test
    void deleteLesson_callsRepository() {
        doNothing().when(lessonRepository).deleteById(1L);
        service.deleteLesson(1L);
        verify(lessonRepository).deleteById(1L);
    }
}
