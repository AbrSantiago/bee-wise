package com.beewise.service.impl;

import com.beewise.controller.dto.*;
import com.beewise.exception.UserNotFoundException;
import com.beewise.model.ItemCategory;
import com.beewise.model.Lesson;
import com.beewise.model.User;
import com.beewise.model.challenge.ChallengeStatus;
import com.beewise.repository.UserRepository;
import com.beewise.service.LessonService;
import com.beewise.service.ShopService;
import com.beewise.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LessonService lessonService;

    @Autowired
    private ShopService shopService;

    private User testUser;
    private Lesson testLesson;

    @BeforeEach
    void setUp() {
        createItem("Default skin", ItemCategory.SKIN);
        createItem("Default hair", ItemCategory.HAIR);
        createItem("Default shirt", ItemCategory.SHIRT);
        createItem("Default background", ItemCategory.BACKGROUND);
        testUser = createTestUser("testUser", "test@example.com");
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

    private User createTestUser(String username, String email) {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setUsername(username);
        dto.setEmail(email);
        dto.setName("Test");
        dto.setSurname("User");
        dto.setPassword("password123");
        return userService.registerUser(dto);
    }

    private Lesson createTestLesson() {
        SimpleLessonDTO dto = new SimpleLessonDTO();
        dto.setTitle("Test Lesson");
        dto.setDescription("Test Description");
        return lessonService.createLesson(dto);
    }

    @Test
    void registerUser_validData_createsUser() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setUsername("newUser");
        dto.setEmail("newuser@test.com");
        dto.setName("New");
        dto.setSurname("User");
        dto.setPassword("password123");

        User result = userService.registerUser(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("newUser", result.getUsername());
        assertEquals("newuser@test.com", result.getEmail());
        assertEquals("New", result.getName());
        assertEquals("User", result.getSurname());
        assertEquals(0, result.getPoints());
    }

    @Test
    void registerUser_duplicateEmail_throwsException() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setUsername("anotherUser");
        dto.setEmail(testUser.getEmail()); // Email duplicated
        dto.setName("Another");
        dto.setSurname("User");
        dto.setPassword("password123");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(dto)
        );

        assertEquals("Email already exists", exception.getMessage());
    }

    @Test
    void registerUser_duplicateUsername_throwsException() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setUsername(testUser.getUsername()); // Username duplicated
        dto.setEmail("different@test.com");
        dto.setName("Different");
        dto.setSurname("User");
        dto.setPassword("password123");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(dto)
        );

        assertEquals("Username already exists", exception.getMessage());
    }

    @Test
    void authenticateUser_validCredentials_returnsUser() {
        LoginUserDTO dto = new LoginUserDTO();
        dto.setUsername(testUser.getUsername());
        dto.setPassword("password123");

        User result = userService.authenticateUser(dto);

        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getUsername(), result.getUsername());
    }

    @Test
    void authenticateUser_invalidUsername_throwsException() {
        LoginUserDTO dto = new LoginUserDTO();
        dto.setUsername("nonexistent");
        dto.setPassword("password123");

        BadCredentialsException exception = assertThrows(
                BadCredentialsException.class,
                () -> userService.authenticateUser(dto)
        );

        assertEquals("Invalid username or password", exception.getMessage());
    }

    @Test
    void authenticateUser_invalidPassword_throwsException() {
        LoginUserDTO dto = new LoginUserDTO();
        dto.setUsername(testUser.getUsername());
        dto.setPassword("WrongPassword");

        BadCredentialsException exception = assertThrows(
                BadCredentialsException.class,
                () -> userService.authenticateUser(dto)
        );

        assertEquals("Invalid username or password", exception.getMessage());
    }

    @Test
    void getUserById_existingUser_returnsUser() {
        User result = userService.getUserById(testUser.getId());

        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getUsername(), result.getUsername());
    }

    @Test
    void getUserById_nonExistentUser_throwsException() {
        Long nonExistentId = 999999L;

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.getUserById(nonExistentId)
        );

        assertEquals("User not found with id: " + nonExistentId, exception.getMessage());
    }

    @Test
    void getUserByUsername_existingUser_returnsUser() {
        User result = userService.getUserByUsername(testUser.getUsername());

        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getUsername(), result.getUsername());
    }

    @Test
    void getUserByUsername_nonExistentUser_throwsException() {
        String nonExistentUsername = "nonexistent";

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.getUserByUsername(nonExistentUsername)
        );

        assertEquals("User not found with username: " + nonExistentUsername, exception.getMessage());
    }

    @Test
    void getAllUsers_returnsAllUsers() {
        List<User> allUsers = userService.getAllUsers();

        assertNotNull(allUsers);
        assertFalse(allUsers.isEmpty());
        assertTrue(allUsers.stream().anyMatch(u -> u.getId().equals(testUser.getId())));
    }

    @Test
    void getUserPoints_existingUser_returnsPoints() {
        UserPointsDTO result = userService.getUserPoints(testUser.getUsername());

        assertNotNull(result);
        assertEquals(testUser.getId(), result.getUserId());
        assertEquals(testUser.getUsername(), result.getUsername());
        assertEquals(testUser.getPoints(), result.getPoints());
        assertEquals(testUser.getCurrentLesson(), result.getCurrentLesson());
    }

    @Test
    void getUserPoints_nonExistentUser_throwsException() {
        String nonExistentUsername = "nonexistent";

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.getUserPoints(nonExistentUsername)
        );

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void lessonComplete_validData_updatesUserProgressAndPoints() {
        int initialPoints = testUser.getPoints();

        LessonCompleteRequestDTO requestDTO = new LessonCompleteRequestDTO();
        requestDTO.setUserId(testUser.getId());
        requestDTO.setCompletedLessonId(testLesson.getId());
        requestDTO.setCorrectExercises(5);

        LessonCompleteDTO result = userService.lessonComplete(requestDTO);

        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("Progress updated", result.getMessage());

        User updatedUser = userRepository.findById(testUser.getId()).orElse(null);
        assertNotNull(updatedUser);
        assertEquals(initialPoints + 50, updatedUser.getPoints()); // 5 exercises * 10 puntos
        assertEquals(1, updatedUser.getCurrentLesson()); // +1 from start value
    }

    @Test
    void lessonComplete_nonExistentUser_throwsException() {
        LessonCompleteRequestDTO requestDTO = new LessonCompleteRequestDTO();
        requestDTO.setUserId(999999L);
        requestDTO.setCompletedLessonId(testLesson.getId());
        requestDTO.setCorrectExercises(3);

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.lessonComplete(requestDTO)
        );

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void lessonComplete_zeroCorrectExercises_updatesWithZeroPoints() {
        int initialPoints = testUser.getPoints();

        LessonCompleteRequestDTO requestDTO = new LessonCompleteRequestDTO();
        requestDTO.setUserId(testUser.getId());
        requestDTO.setCompletedLessonId(testLesson.getId());
        requestDTO.setCorrectExercises(0);

        LessonCompleteDTO result = userService.lessonComplete(requestDTO);

        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("Progress updated", result.getMessage());

        // Verify that points did not change on DB
        User updatedUser = userRepository.findById(testUser.getId()).orElse(null);
        assertNotNull(updatedUser);
        assertEquals(initialPoints, updatedUser.getPoints()); // Sin cambio en puntos
    }

    @Test
    void lessonComplete_multipleCorrectExercises_calculatesPointsCorrectly() {
        int initialPoints = testUser.getPoints();

        LessonCompleteRequestDTO requestDTO = new LessonCompleteRequestDTO();
        requestDTO.setUserId(testUser.getId());
        requestDTO.setCompletedLessonId(testLesson.getId());
        requestDTO.setCorrectExercises(10);

        LessonCompleteDTO result = userService.lessonComplete(requestDTO);

        assertNotNull(result);
        assertTrue(result.isSuccess());

        // Verify on DB that user points are correctly calculated
        User updatedUser = userRepository.findById(testUser.getId()).orElse(null);
        assertNotNull(updatedUser);
        assertEquals(initialPoints + 100, updatedUser.getPoints()); // 10 * 10 = 100 points
    }

    @Test
    void getUsersToChallenge_withActiveStatuses_returnsAvailableUsers() {
        createTestUser("challenger", "challenger@test.com");
        createTestUser("available", "available@test.com");

        List<ChallengeStatus> activeStatuses = Arrays.asList(
                ChallengeStatus.PENDING,
                ChallengeStatus.ACTIVE,
                ChallengeStatus.COMPLETED,
                ChallengeStatus.EXPIRED
        );

        List<User> result = userService.getUsersToChallenge(testUser.getId(), activeStatuses);

        assertNotNull(result);
        // Verify that no includes user who is searching challengers
        assertFalse(result.stream().anyMatch(u -> u.getId().equals(testUser.getId())));
        // Should include other users
    }

    @Test
    void getUsersToChallenge_emptyActiveStatuses_returnsAvailableUsers() {
        List<ChallengeStatus> emptyStatuses = List.of();
        List<User> result = userService.getUsersToChallenge(testUser.getId(), emptyStatuses);
        assertNotNull(result);
    }

    @Test
    void getUsersToChallenge_nonExistentChallenger_returnsResults() {
        Long nonExistentId = 999999L;
        List<ChallengeStatus> activeStatuses = List.of(ChallengeStatus.PENDING);

        List<User> result = userService.getUsersToChallenge(nonExistentId, activeStatuses);

        assertNotNull(result);
    }

    @Test
    void contextLoads() {
        assertNotNull(userService);
        assertNotNull(userRepository);
    }
}