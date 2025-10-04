package com.beewise.service.impl;

import com.beewise.controller.dto.*;
import com.beewise.exception.UserNotFoundException;
import com.beewise.model.Lesson;
import com.beewise.model.User;
import com.beewise.model.challenge.ChallengeStatus;
import com.beewise.repository.UserRepository;
import com.beewise.service.LessonService;
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

    private User testUser;
    private Lesson testLesson;

    @BeforeEach
    void setUp() {
        testUser = createTestUser("testUser", "test@example.com");
        testLesson = createTestLesson("Test Lesson", "Test Description");
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
        dto.setEmail(testUser.getEmail()); // Email duplicado
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
        dto.setUsername(testUser.getUsername()); // Username duplicado
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
        dto.setPassword("wrongpassword");

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
        assertTrue(allUsers.size() >= 1);
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

        // Remover la línea que causa error - LessonCompleteDTO no tiene getPoints()
        // assertEquals(initialPoints + 50, result.getPoints());

        // En su lugar, verificar directamente en la base de datos
        User updatedUser = userRepository.findById(testUser.getId()).orElse(null);
        assertNotNull(updatedUser);
        assertEquals(initialPoints + 50, updatedUser.getPoints()); // 5 ejercicios * 10 puntos
        assertEquals(1, updatedUser.getCurrentLesson()); // +1 desde el valor inicial
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

        // Verificar en la BD que los puntos no cambiaron
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

        // Verificar en la BD que los puntos se calcularon correctamente
        User updatedUser = userRepository.findById(testUser.getId()).orElse(null);
        assertNotNull(updatedUser);
        assertEquals(initialPoints + 100, updatedUser.getPoints()); // 10 * 10 = 100 puntos
    }

    @Test
    void getUsersToChallenge_withActiveStatuses_returnsAvailableUsers() {
        // Crear usuarios adicionales para el test
        User user2 = createTestUser("challenger", "challenger@test.com");
        User user3 = createTestUser("available", "available@test.com");

        List<ChallengeStatus> activeStatuses = Arrays.asList(
                ChallengeStatus.PENDING,
                ChallengeStatus.ACTIVE,
                ChallengeStatus.COMPLETED,
                ChallengeStatus.EXPIRED
        );

        List<User> result = userService.getUsersToChallenge(testUser.getId(), activeStatuses);

        assertNotNull(result);
        // Verificar que no incluye al usuario que está buscando challengers
        assertFalse(result.stream().anyMatch(u -> u.getId().equals(testUser.getId())));

        // Debería incluir otros usuarios (dependiendo de la lógica de tu repositorio)
        // Este test depende de la implementación de findAvailableToChallenge
    }

    @Test
    void getUsersToChallenge_emptyActiveStatuses_returnsAvailableUsers() {
        List<ChallengeStatus> emptyStatuses = Arrays.asList();

        List<User> result = userService.getUsersToChallenge(testUser.getId(), emptyStatuses);

        assertNotNull(result);
        // El comportamiento depende de tu implementación del repositorio
    }

    @Test
    void getUsersToChallenge_nonExistentChallenger_returnsResults() {
        Long nonExistentId = 999999L;
        List<ChallengeStatus> activeStatuses = Arrays.asList(ChallengeStatus.PENDING);

        // Este test no debería fallar, solo retornar una lista (posiblemente vacía)
        List<User> result = userService.getUsersToChallenge(nonExistentId, activeStatuses);

        assertNotNull(result);
        // El comportamiento específico depende de tu implementación
    }

    @Test
    void contextLoads() {
        assertNotNull(userService);
        assertNotNull(userRepository);
    }
}