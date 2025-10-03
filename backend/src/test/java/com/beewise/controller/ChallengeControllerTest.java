package com.beewise.controller;

import com.beewise.controller.dto.*;
import com.beewise.model.User;
import com.beewise.model.challenge.*;
import com.beewise.service.ChallengeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;// ✅ Cambiar esta importación
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChallengeController.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ChallengeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean  // ✅ Cambiar de @MockitoBean a @MockitoBean
    private ChallengeService challengeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void getAll_returnsChallengeList() throws Exception {
        User challenger = createUser(1L, "challenger");
        User challenged = createUser(2L, "challenged");

        Challenge challenge = createChallenge(10L, challenger, challenged);
        List<Challenge> challenges = Arrays.asList(challenge);

        when(challengeService.getAll()).thenReturn(challenges);

        mockMvc.perform(get("/challenge"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser
    void getAll_withEmptyList_returnsEmptyArray() throws Exception {
        when(challengeService.getAll()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/challenge"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @WithMockUser
    void getUsersToChallenge_returnsUserList() throws Exception {
        User user = createUser(1L, "testuser");
        List<User> users = Arrays.asList(user);

        when(challengeService.getUsersToChallenge(5L)).thenReturn(users);

        mockMvc.perform(get("/challenge/usersToChallenge/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser
    void getUsersToChallenge_withEmptyList_returnsEmptyArray() throws Exception {
        when(challengeService.getUsersToChallenge(10L)).thenReturn(Arrays.asList());

        mockMvc.perform(get("/challenge/usersToChallenge/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @WithMockUser
    void sendChallenge_withValidData_returnsChallenge() throws Exception {
        SendChallengeDTO dto = new SendChallengeDTO();

        User challenger = createUser(1L, "challenger");
        User challenged = createUser(2L, "challenged");
        Challenge challenge = createChallenge(100L, challenger, challenged);

        when(challengeService.sendChallenge(any(SendChallengeDTO.class))).thenReturn(challenge);

        mockMvc.perform(post("/challenge/send")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void sendChallenge_withInvalidData_returnsBadRequest() throws Exception {
        SendChallengeDTO invalidDTO = new SendChallengeDTO();

        mockMvc.perform(post("/challenge/send")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void acceptChallenge_returnsAcceptedChallenge() throws Exception {
        User challenger = createUser(1L, "challenger");
        User challenged = createUser(2L, "challenged");
        Challenge challenge = createChallenge(50L, challenger, challenged);

        when(challengeService.acceptChallenge(50L)).thenReturn(challenge);

        mockMvc.perform(post("/challenge/accept/50")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void acceptChallenge_withDifferentId_callsServiceCorrectly() throws Exception {
        User challenger = createUser(3L, "challenger3");
        User challenged = createUser(4L, "challenged4");
        Challenge challenge = createChallenge(75L, challenger, challenged);

        when(challengeService.acceptChallenge(75L)).thenReturn(challenge);

        mockMvc.perform(post("/challenge/accept/75")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void answerRound_returnsUpdatedChallenge() throws Exception {
        AnswerDTO answerDTO = new AnswerDTO();

        User challenger = createUser(1L, "challenger");
        User challenged = createUser(2L, "challenged");
        Challenge challenge = createChallenge(30L, challenger, challenged);

        when(challengeService.answerRound(any(AnswerDTO.class))).thenReturn(challenge);

        mockMvc.perform(post("/challenge/answer")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(answerDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void answerRound_withInvalidAnswer_returnsBadRequest() throws Exception {
        AnswerDTO invalidAnswerDTO = new AnswerDTO();

        mockMvc.perform(post("/challenge/answer")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidAnswerDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void constructor_initializesController() {
        ChallengeController controller = new ChallengeController(challengeService);
        assertNotNull(controller);
    }

    @Test
    @WithMockUser
    void getUsersToChallenge_withDifferentChallengerId_callsServiceCorrectly() throws Exception {
        User user = createUser(10L, "testuser");
        List<User> users = Arrays.asList(user);

        when(challengeService.getUsersToChallenge(99L)).thenReturn(users);

        mockMvc.perform(get("/challenge/usersToChallenge/99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    // Helper methods
    private User createUser(Long id, String username) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(username + "@test.com");
        user.setName("Name" + id);
        user.setSurname("Surname" + id);
        return user;
    }

    private Challenge createChallenge(Long id, User challenger, User challenged) {
        Challenge challenge = new Challenge();
        challenge.setId(id);
        challenge.setChallenger(challenger);
        challenge.setChallenged(challenged);
        challenge.setStatus(ChallengeStatus.PENDING);
        challenge.setMaxRounds(3);
        challenge.setQuestionsPerRound(5);
        challenge.setCreationDate(LocalDate.now());
        challenge.setExpireDate(LocalDate.now().plusDays(7));
        challenge.setRounds(Arrays.asList());
        return challenge;
    }
}