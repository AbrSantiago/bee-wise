package com.beewise.service.impl;

import com.beewise.controller.dto.RegisterUserDTO;
import com.beewise.controller.dto.SimpleLessonDTO;
import com.beewise.model.Lesson;
import com.beewise.model.LessonProgress;
import com.beewise.model.User;
import com.beewise.repository.LessonProgressRepository;
import com.beewise.service.LessonProgressService;
import com.beewise.service.LessonService;
import com.beewise.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class LessonProgressServiceImplTest {

    @Autowired
    private LessonProgressService progressService;

    @Autowired
    private LessonProgressRepository progressRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private LessonService lessonService;

    private User testUser;
    private Lesson testLesson;

    @BeforeEach
    void setUp() {
        testUser = createTestUser("progressUser", "progress@test.com");
        testLesson = createTestLesson("Progress Lesson", "Progress Description");
    }

    private User createTestUser(String username, String email) {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setUsername(username);
        dto.setEmail(email);
        dto.setName("Test");
        dto.setSurname("User");
        dto.setPassword("password123");
        return userService.registerUser(dto);
    }

    private Lesson createTestLesson(String title, String description) {
        SimpleLessonDTO dto = new SimpleLessonDTO();
        dto.setTitle(title);
        dto.setDescription(description);
        return lessonService.createLesson(dto);
    }

    @Test
    void upsertProgress_newProgress_createsProgress() {
        long progressCountBefore = progressRepository.count();

        progressService.upsertProgress(testUser, testLesson);

        long progressCountAfter = progressRepository.count();
        assertEquals(progressCountBefore + 1, progressCountAfter);

        Optional<LessonProgress> savedProgress = progressRepository
                .findByUser_IdAndLesson_Id(testUser.getId(), testLesson.getId());
        assertTrue(savedProgress.isPresent());
        assertEquals(testUser.getId(), savedProgress.get().getUser().getId());
        assertEquals(testLesson.getId(), savedProgress.get().getLesson().getId());
        assertEquals(1, savedProgress.get().getAttempts());
    }

    @Test
    void upsertProgress_multipleCallsOnSameLesson_handlesCorrectly() {
        progressService.upsertProgress(testUser, testLesson);

        Optional<LessonProgress> firstProgress = progressRepository
                .findByUser_IdAndLesson_Id(testUser.getId(), testLesson.getId());
        assertTrue(firstProgress.isPresent());
        Long progressId = firstProgress.get().getId();

        progressService.upsertProgress(testUser, testLesson);

        Optional<LessonProgress> updatedProgress = progressRepository
                .findByUser_IdAndLesson_Id(testUser.getId(), testLesson.getId());
        assertTrue(updatedProgress.isPresent());
        assertEquals(progressId, updatedProgress.get().getId(), "Debería ser el mismo registro de progreso");
    }

    @Test
    void contextLoads() {
        assertNotNull(progressService);
        assertNotNull(progressRepository);
        assertNotNull(userService);
        assertNotNull(lessonService);
    }
}