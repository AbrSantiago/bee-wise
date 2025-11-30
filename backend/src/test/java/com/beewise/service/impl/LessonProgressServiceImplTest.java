package com.beewise.service.impl;

import com.beewise.controller.dto.NewShopItemDTO;
import com.beewise.controller.dto.RegisterUserDTO;
import com.beewise.controller.dto.SimpleLessonDTO;
import com.beewise.model.*;
import com.beewise.repository.LessonProgressRepository;
import com.beewise.repository.LevelRepository;
import com.beewise.service.LessonProgressService;
import com.beewise.service.LessonService;
import com.beewise.service.ShopService;
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

    @Autowired
    private LevelRepository levelRepository;

    @Autowired
    private ShopService shopService;

    private User testUser;
    private Lesson testLesson;

    @BeforeEach
    void setUp() {

        createDefaultLevels();

        createItem("Default skin", ItemCategory.SKIN);
        createItem("Default hair", ItemCategory.HAIR);
        createItem("Default shirt", ItemCategory.SHIRT);
        createItem("Default background", ItemCategory.BACKGROUND);
        testUser = createTestUser();
        testLesson = createTestLesson();
    }

    private void createItem(String name, ItemCategory category) {
        NewShopItemDTO item = new NewShopItemDTO();
        item.setName(name);
        item.setCategory(category);
        item.setImage("");
        item.setPrice(0);
        shopService.createShopItem(item);
    }

    private User createTestUser() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setUsername("progressUser");
        dto.setEmail("progress@test.com");
        dto.setName("Test");
        dto.setSurname("User");
        dto.setPassword("password123");
        return userService.registerUser(dto);
    }

    private Lesson createTestLesson() {
        SimpleLessonDTO dto = new SimpleLessonDTO();
        dto.setTitle("Progress Lesson");
        dto.setDescription("Progress Description");
        return lessonService.createLesson(dto);
    }

    private void createDefaultLevels() {
        if (levelRepository.count() == 0) {
            Level level1 = new Level();
            level1.setName("Beginner");
            level1.setMinPoints(0);
            level1.setIconUrl("icon1.png");
            levelRepository.save(level1);

            Level level2 = new Level();
            level2.setName("Intermediate");
            level2.setMinPoints(100);
            level2.setIconUrl("icon2.png");
            levelRepository.save(level2);
        }
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
        assertEquals(progressId, updatedProgress.get().getId(), "Should be same progress register");
    }

    @Test
    void contextLoads() {
        assertNotNull(progressService);
        assertNotNull(progressRepository);
        assertNotNull(userService);
        assertNotNull(lessonService);
    }
}