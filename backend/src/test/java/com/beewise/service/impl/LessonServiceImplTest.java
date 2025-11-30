package com.beewise.service.impl;

import com.beewise.controller.dto.SimpleLessonDTO;
import com.beewise.exception.InvalidIdException;
import com.beewise.exception.LessonNotFoundException;
import com.beewise.exception.ExerciseNotFoundException;
import com.beewise.model.Exercise;
import com.beewise.model.ExerciseCategory;
import com.beewise.model.Lesson;
import com.beewise.model.OpenExercise;
import com.beewise.repository.ExerciseRepository;
import com.beewise.repository.LessonRepository;
import com.beewise.service.LessonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class LessonServiceImplTest {

    @Autowired
    private LessonService lessonService;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    private Lesson testLesson;
    private Exercise testExercise;

    @BeforeEach
    void setUp() {
        testLesson = createTestLesson("Test Lesson", "Test Description");
        testExercise = createTestExercise("Test Question", "Test Answer", ExerciseCategory.MATRICES);
    }

    private Lesson createTestLesson(String title, String description) {
        SimpleLessonDTO dto = new SimpleLessonDTO();
        dto.setTitle(title);
        dto.setDescription(description);
        return lessonService.createLesson(dto);
    }

    private Exercise createTestExercise(String question, String answer, ExerciseCategory category) {
        Exercise exercise = new OpenExercise(question, answer, category);
        return exerciseRepository.save(exercise);
    }

    @Test
    void getLessonById_validId_returnsLesson() {
        Lesson result = lessonService.getLessonById(testLesson.getId());

        assertNotNull(result);
        assertEquals(testLesson.getId(), result.getId());
        assertEquals(testLesson.getTitle(), result.getTitle());
        assertEquals(testLesson.getDescription(), result.getDescription());
    }

    @Test
    void getLessonById_invalidId_throwsException() {
        InvalidIdException exception = assertThrows(
                InvalidIdException.class,
                () -> lessonService.getLessonById(-1L)
        );

        assertEquals("Lesson id must be a positive number", exception.getMessage());
    }

    @Test
    void getLessonById_notFound_throwsException() {
        Long nonExistentId = 999999L;

        LessonNotFoundException exception = assertThrows(
                LessonNotFoundException.class,
                () -> lessonService.getLessonById(nonExistentId)
        );

        assertEquals("Lesson with id " + nonExistentId + " not found", exception.getMessage());
    }

    @Test
    void createLesson_validData_savesAndReturns() {
        SimpleLessonDTO dto = new SimpleLessonDTO();
        dto.setTitle("New Lesson");
        dto.setDescription("New Description");

        Lesson result = lessonService.createLesson(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("New Lesson", result.getTitle());
        assertEquals("New Description", result.getDescription());
    }

    @Test
    void getAllLessons_returnsAllLessons() {
        List<Lesson> allLessons = lessonService.getAllLessons();

        assertNotNull(allLessons);
        assertTrue(allLessons.size() >= 1);
        assertTrue(allLessons.stream().anyMatch(l -> l.getId().equals(testLesson.getId())));
    }

    @Test
    void updateLesson_existingLesson_updatesFields() {
        SimpleLessonDTO dto = new SimpleLessonDTO();
        dto.setTitle("Updated Title");
        dto.setDescription("Updated Description");

        Lesson result = lessonService.updateLesson(testLesson.getId(), dto);

        assertNotNull(result);
        assertEquals(testLesson.getId(), result.getId());
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
    }

    @Test
    void updateLesson_notFound_throwsException() {
        Long nonExistentId = 999999L;
        SimpleLessonDTO dto = new SimpleLessonDTO();
        dto.setTitle("Updated");
        dto.setDescription("Updated");

        LessonNotFoundException exception = assertThrows(
                LessonNotFoundException.class,
                () -> lessonService.updateLesson(nonExistentId, dto)
        );

        assertEquals("Lesson with id " + nonExistentId + " not found", exception.getMessage());
    }

    @Test
    void addExercise_validIds_addsExercise() {
        Lesson result = lessonService.addExercise(testLesson.getId(), testExercise.getId());

        assertNotNull(result);
        assertTrue(result.getExercises().stream()
                .anyMatch(e -> e.getId().equals(testExercise.getId())));

        Exercise updatedExercise = exerciseRepository.findById(testExercise.getId()).orElse(null);
        assertNotNull(updatedExercise);
        assertEquals(testLesson.getId(), updatedExercise.getLesson().getId());
    }

    @Test
    void addExercise_invalidLessonId_throwsException() {
        InvalidIdException exception = assertThrows(
                InvalidIdException.class,
                () -> lessonService.addExercise(-1L, testExercise.getId())
        );

        assertEquals("Lesson id must be a positive number", exception.getMessage());
    }

    @Test
    void addExercise_invalidExerciseId_throwsException() {
        InvalidIdException exception = assertThrows(
                InvalidIdException.class,
                () -> lessonService.addExercise(testLesson.getId(), -1L)
        );

        assertEquals("Exercise id must be a positive number", exception.getMessage());
    }

    @Test
    void addExercise_alreadyExists_throwsException() {
        lessonService.addExercise(testLesson.getId(), testExercise.getId());

        InvalidIdException exception = assertThrows(
                InvalidIdException.class,
                () -> lessonService.addExercise(testLesson.getId(), testExercise.getId())
        );

        assertEquals("Exercise with id " + testExercise.getId() + " is already in the lesson",
                exception.getMessage());
    }

    @Test
    void removeExercise_validIds_removesExercise() {
        lessonService.addExercise(testLesson.getId(), testExercise.getId());

        Lesson result = lessonService.removeExercise(testLesson.getId(), testExercise.getId());

        assertNotNull(result);
        assertFalse(result.getExercises().stream()
                .anyMatch(e -> e.getId().equals(testExercise.getId())));

        Exercise updatedExercise = exerciseRepository.findById(testExercise.getId()).orElse(null);
        assertNotNull(updatedExercise);
        assertNull(updatedExercise.getLesson());
    }

    @Test
    void removeExercise_exerciseNotInLesson_throwsException() {
        InvalidIdException exception = assertThrows(
                InvalidIdException.class,
                () -> lessonService.removeExercise(testLesson.getId(), testExercise.getId())
        );

        assertEquals("Exercise with id " + testExercise.getId() + " is not in the lesson",
                exception.getMessage());
    }

    @Test
    void deleteLesson_validId_deletesLesson() {
        Long lessonId = testLesson.getId();

        lessonService.deleteLesson(lessonId);

        assertFalse(lessonRepository.existsById(lessonId));
    }

    @Test
    void contextLoads() {
        assertNotNull(lessonService);
        assertNotNull(lessonRepository);
        assertNotNull(exerciseRepository);
    }
}