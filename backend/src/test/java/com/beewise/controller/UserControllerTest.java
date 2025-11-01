package com.beewise.controller;

import com.beewise.controller.dto.*;
import com.beewise.model.Avatar;
import com.beewise.model.ItemCategory;
import com.beewise.model.ShopItem;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private TokenServiceImpl tokenService;

    @MockBean
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
        registerDTO.setUsername("JohnDoe");
        registerDTO.setPassword("password123");

        User createdUser = new User();
        createdUser.setId(1L);
        createdUser.setName("John");
        createdUser.setSurname("Doe");
        createdUser.setEmail("john@example.com");
        createdUser.setUsername("JohnDoe");

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
                .andExpect(jsonPath("$.username").value("JohnDoe"));
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
        loginDTO.setUsername("JohnDoe");
        loginDTO.setPassword("password123");

        User user = new User();
        user.setId(1L);
        user.setUsername("JohnDoe");
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
                .andExpect(jsonPath("$.username").value("JohnDoe"));
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
        user.setUsername("JohnDoe");
        user.setPoints(100);
        user.setCurrentLesson(5);
        user.setAvatar(createAvatar(user));

        when(jwtService.extractUsername("valid-token")).thenReturn("JohnDoe");
        when(userService.getUserByUsername("JohnDoe")).thenReturn(user);

        mockMvc.perform(get("/users/me")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.surname").value("Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.username").value("JohnDoe"))
                .andExpect(jsonPath("$.points").value(100))
                .andExpect(jsonPath("$.currentLesson").value(5));
    }

    @Test
    @WithMockUser
    void getAllUsers_returnsListOfUsers() throws Exception {
        User user1 = new User();
        user1.setId(1L);
        user1.setName("John");
        user1.setUsername("JohnDoe");
        user1.setAvatar(createAvatar(user1));

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Jane");
        user2.setUsername("JaneDoe");
        user2.setAvatar(createAvatar(user2));

        List<User> users = Arrays.asList(user1, user2);

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users")
                        .header("Authorization", "Bearer mock-jwt-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[0].username").value("JohnDoe"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Jane"))
                .andExpect(jsonPath("$[1].username").value("JaneDoe"));
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
        userPointsDTO.setUsername("JohnDoe");
        userPointsDTO.setPoints(250);

        when(jwtService.extractUsername("valid-token")).thenReturn("JohnDoe");
        when(userService.getUserPoints("JohnDoe")).thenReturn(userPointsDTO);

        mockMvc.perform(get("/users/points")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("JohnDoe"))
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
        when(userService.getAllUsers()).thenReturn(List.of());

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
        user.setUsername("TestUser");
        user.setAvatar(createAvatar(user));

        when(jwtService.extractUsername("extracted-token")).thenReturn("TestUser");
        when(userService.getUserByUsername("TestUser")).thenReturn(user);

        mockMvc.perform(get("/users/me")
                        .header("Authorization", "Bearer extracted-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.username").value("TestUser"));
    }

    @Test
    @WithMockUser
    void getUserPoints_extractsTokenCorrectly() throws Exception {
        UserPointsDTO userPointsDTO = new UserPointsDTO();
        userPointsDTO.setUsername("AnotherUser");
        userPointsDTO.setPoints(500);

        when(jwtService.extractUsername("another-token")).thenReturn("AnotherUser");
        when(userService.getUserPoints("AnotherUser")).thenReturn(userPointsDTO);

        mockMvc.perform(get("/users/points")
                        .header("Authorization", "Bearer another-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("AnotherUser"))
                .andExpect(jsonPath("$.points").value(500));
    }

    @Test
    @WithMockUser
    void updateAvatar_withValidRequest_returnsUpdatedUser() throws Exception {
        Long userId = 1L;
        AvatarDTO avatarDTO = new AvatarDTO();
        avatarDTO.setId(10L);

        User user = new User();
        user.setId(userId);
        user.setUsername("JohnDoe");
        user.setAvatar(createAvatar(user)); // Usa solo el helper, no sobrescribas

        when(userService.updateAvatar(eq(userId), any(AvatarDTO.class))).thenReturn(user);

        mockMvc.perform(put("/users/updateAvatar/{userId}", userId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(avatarDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.username").value("JohnDoe"));
    }

    @Test
    @WithMockUser
    void getUserItems_withValidToken_returnsShopItemDTOList() throws Exception {
        ShopItem item1 = new ShopItem();
        item1.setId(1L);
        ShopItem item2 = new ShopItem();
        item2.setId(2L);

        when(jwtService.extractUsername("valid-token")).thenReturn("JohnDoe");
        when(userService.getUserItems("JohnDoe")).thenReturn(List.of(item1, item2));

        mockMvc.perform(get("/users/items")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    @WithMockUser
    void getUserItemsByCategory_withValidToken_returnsGroupedItems() throws Exception {
        ShopItem item1 = new ShopItem();
        item1.setId(1L);
        ShopItem item2 = new ShopItem();
        item2.setId(2L);

        Map<ItemCategory, List<ShopItem>> groupedItems = Map.of(
                ItemCategory.BACKGROUND, List.of(item1),
                ItemCategory.HAIR, List.of(item2)
        );

        when(jwtService.extractUsername("valid-token")).thenReturn("JohnDoe");
        when(userService.getAllByCategory("JohnDoe")).thenReturn(groupedItems);

        mockMvc.perform(get("/users/allItemsByCategory")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.BACKGROUND[0].id").value(1L))
                .andExpect(jsonPath("$.HAIR[0].id").value(2L));
    }

    // ============ HELPERS ============

    private Avatar createAvatar(User user) {
        Avatar avatar = new Avatar();
        avatar.setUser(user);

        ShopItem background = new ShopItem();
        background.setId(1L);
        ShopItem shirt = new ShopItem();
        shirt.setId(2L);
        ShopItem skin = new ShopItem();
        skin.setId(3L);
        ShopItem hair = new ShopItem();
        hair.setId(4L);

        avatar.setBackground(background);
        avatar.setShirt(shirt);
        avatar.setSkin(skin);
        avatar.setHair(hair);

        return avatar;
    }

}