package com.beewise.controller;

import com.beewise.controller.dto.*;
import com.beewise.model.User;
import com.beewise.service.UserService;
import com.beewise.service.impl.JwtService;
import com.beewise.service.impl.TokenServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserController.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private TokenServiceImpl tokenService;

    @MockitoBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void registerUser_withValidData_returnsCreatedUser() throws Exception {
        RegisterUserDTO registerDTO = new RegisterUserDTO();
        registerDTO.setName("John");
        registerDTO.setSurname("Doe");
        registerDTO.setEmail("john@example.com");
        registerDTO.setUsername("johndoe");
        registerDTO.setPassword("password123");

        User createdUser = new User();
        createdUser.setId(1L);
        createdUser.setName("John");
        createdUser.setSurname("Doe");
        createdUser.setEmail("john@example.com");
        createdUser.setUsername("johndoe");

        when(userService.registerUser(any(RegisterUserDTO.class))).thenReturn(createdUser);

        mockMvc.perform(post("/users/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.surname").value("Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.username").value("johndoe"));
    }

    @Test
    @WithMockUser
    void registerUser_withInvalidData_returnsBadRequest() throws Exception {
        RegisterUserDTO invalidDTO = new RegisterUserDTO();

        mockMvc.perform(post("/users/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void login_withValidCredentials_returnsLoginResponse() throws Exception {
        LoginUserDTO loginDTO = new LoginUserDTO();
        loginDTO.setUsername("johndoe");
        loginDTO.setPassword("password123");

        User user = new User();
        user.setId(1L);
        user.setUsername("johndoe");
        user.setEmail("john@example.com");

        when(userService.authenticateUser(any(LoginUserDTO.class))).thenReturn(user);
        when(jwtService.generateAccessToken(anyString())).thenReturn("access-token");
        when(jwtService.generateRefreshToken(anyString())).thenReturn("refresh-token");

        mockMvc.perform(post("/users/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.username").value("johndoe"));
    }

    @Test
    @WithMockUser
    void login_withInvalidCredentials_returnsBadRequest() throws Exception {
        LoginUserDTO invalidLoginDTO = new LoginUserDTO();

        mockMvc.perform(post("/users/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidLoginDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void refreshToken_withValidToken_returnsNewTokens() throws Exception {
        RefreshTokenRequestDTO requestDTO = new RefreshTokenRequestDTO();
        requestDTO.setRefreshToken("valid-refresh-token");

        RefreshTokenResponseDTO responseDTO = new RefreshTokenResponseDTO();
        responseDTO.setAccessTokenToken("new-access-token");
        responseDTO.setRefreshToken("new-refresh-token");

        when(tokenService.rotateRefreshToken(anyString())).thenReturn(responseDTO);

        mockMvc.perform(post("/users/auth/refresh")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessTokenToken").value("new-access-token"))
                .andExpect(jsonPath("$.refreshToken").value("new-refresh-token"));
    }

    @Test
    @WithMockUser
    void getCurrentUser_withValidToken_returnsUserData() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("John");
        user.setSurname("Doe");
        user.setEmail("john@example.com");
        user.setUsername("johndoe");
        user.setPoints(100);
        user.setCurrentLesson(5);

        when(jwtService.extractUsername("valid-token")).thenReturn("johndoe");
        when(userService.getUserByUsername("johndoe")).thenReturn(user);

        mockMvc.perform(get("/users/me")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.surname").value("Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.username").value("johndoe"))
                .andExpect(jsonPath("$.points").value(100))
                .andExpect(jsonPath("$.currentLesson").value(5));
    }

    @Test
    @WithMockUser
    void getAllUsers_returnsListOfUsers() throws Exception {
        User user1 = new User();
        user1.setId(1L);
        user1.setName("John");
        user1.setUsername("johndoe");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Jane");
        user2.setUsername("janedoe");

        List<User> users = Arrays.asList(user1, user2);

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users")
                        .header("Authorization", "Bearer mock-jwt-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[0].username").value("johndoe"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Jane"))
                .andExpect(jsonPath("$[1].username").value("janedoe"));
    }

    @Test
    @WithMockUser
    void lessonComplete_withValidRequest_returnsCompletionData() throws Exception {
        LessonCompleteRequestDTO requestDTO = new LessonCompleteRequestDTO();

        LessonCompleteDTO responseDTO = new LessonCompleteDTO();
        responseDTO.setSuccess(true);
        responseDTO.setMessage("Lesson completed successfully");
        responseDTO.setTotalPoints(150);

        when(userService.lessonComplete(any(LessonCompleteRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/users/lessonComplete")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Lesson completed successfully"))
                .andExpect(jsonPath("$.totalPoints").value(150));
    }

    @Test
    @WithMockUser
    void lessonComplete_withFailure_returnsFailureData() throws Exception {
        LessonCompleteRequestDTO requestDTO = new LessonCompleteRequestDTO();

        LessonCompleteDTO responseDTO = new LessonCompleteDTO();
        responseDTO.setSuccess(false);
        responseDTO.setMessage("Lesson completion failed");
        responseDTO.setTotalPoints(0);

        when(userService.lessonComplete(any(LessonCompleteRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/users/lessonComplete")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Lesson completion failed"))
                .andExpect(jsonPath("$.totalPoints").value(0));
    }

    @Test
    @WithMockUser
    void getUserPoints_withValidToken_returnsUserPoints() throws Exception {
        UserPointsDTO userPointsDTO = new UserPointsDTO();
        userPointsDTO.setUsername("johndoe");
        userPointsDTO.setPoints(250);

        when(jwtService.extractUsername("valid-token")).thenReturn("johndoe");
        when(userService.getUserPoints("johndoe")).thenReturn(userPointsDTO);

        mockMvc.perform(get("/users/points")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("johndoe"))
                .andExpect(jsonPath("$.points").value(250));
    }

    @Test
    void constructor_initializesController() {
        UserController controller = new UserController(userService, tokenService, jwtService);
        assertNotNull(controller);
    }

    @Test
    @WithMockUser
    void getAllUsers_withEmptyList_returnsEmptyArray() throws Exception {
        when(userService.getAllUsers()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/users")
                        .header("Authorization", "Bearer another-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @WithMockUser
    void getCurrentUser_extractsTokenCorrectly() throws Exception {
        User user = new User();
        user.setId(2L);
        user.setUsername("testuser");

        when(jwtService.extractUsername("extracted-token")).thenReturn("testuser");
        when(userService.getUserByUsername("testuser")).thenReturn(user);

        mockMvc.perform(get("/users/me")
                        .header("Authorization", "Bearer extracted-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    @WithMockUser
    void getUserPoints_extractsTokenCorrectly() throws Exception {
        UserPointsDTO userPointsDTO = new UserPointsDTO();
        userPointsDTO.setUsername("anotheruser");
        userPointsDTO.setPoints(500);

        when(jwtService.extractUsername("another-token")).thenReturn("anotheruser");
        when(userService.getUserPoints("anotheruser")).thenReturn(userPointsDTO);

        mockMvc.perform(get("/users/points")
                        .header("Authorization", "Bearer another-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("anotheruser"))
                .andExpect(jsonPath("$.points").value(500));
    }
}