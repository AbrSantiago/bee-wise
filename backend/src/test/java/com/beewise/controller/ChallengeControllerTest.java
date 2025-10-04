package com.beewise.controller;

import com.beewise.controller.dto.*;
import com.beewise.model.User;
import com.beewise.model.challenge.*;
import com.beewise.service.ChallengeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChallengeControllerTest {

    @Mock
    private ChallengeService challengeService;

    private ChallengeController controller;

    @BeforeEach
    void setUp() {
        controller = new ChallengeController(challengeService);
    }

    @Test
    void getAll_returnsChallengeList() {
        User challenger = createUser(1L, "challenger");
        User challenged = createUser(2L, "challenged");
        Challenge challenge1 = createChallenge(10L, challenger, challenged, ChallengeStatus.PENDING);
        Challenge challenge2 = createChallenge(20L, challenged, challenger, ChallengeStatus.ACTIVE);

        List<Challenge> challenges = Arrays.asList(challenge1, challenge2);

        when(challengeService.getAll()).thenReturn(challenges);

        ResponseEntity<List<ChallengeDTO>> response = controller.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(10L, response.getBody().get(0).getId());
        assertEquals(1L, response.getBody().get(0).getChallengerId());
        assertEquals(2L, response.getBody().get(0).getChallengedId());
        assertEquals(ChallengeStatus.PENDING, response.getBody().get(0).getStatus());
        verify(challengeService).getAll();
    }

    @Test
    void getAll_withEmptyList_returnsEmptyList() {
        when(challengeService.getAll()).thenReturn(Arrays.asList());

        ResponseEntity<List<ChallengeDTO>> response = controller.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().size());
        verify(challengeService).getAll();
    }

    @Test
    void getUsersToChallenge_returnsUserList() {
        User user1 = createUser(1L, "user1");
        User user2 = createUser(2L, "user2");
        User user3 = createUser(3L, "user3");

        List<User> users = Arrays.asList(user1, user2, user3);

        when(challengeService.getUsersToChallenge(5L)).thenReturn(users);

        ResponseEntity<List<UserDTO>> response = controller.getUsersToChallenge(5L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().size());
        assertEquals(1L, response.getBody().get(0).getId());
        assertEquals("user1", response.getBody().get(0).getUsername());
        assertEquals("user1@test.com", response.getBody().get(0).getEmail());
        verify(challengeService).getUsersToChallenge(5L);
    }

    @Test
    void getUsersToChallenge_withEmptyList_returnsEmptyList() {
        when(challengeService.getUsersToChallenge(10L)).thenReturn(Arrays.asList());

        ResponseEntity<List<UserDTO>> response = controller.getUsersToChallenge(10L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().size());
        verify(challengeService).getUsersToChallenge(10L);
    }

    @Test
    void sendChallenge_returnsCreatedChallenge() {
        SendChallengeDTO sendChallengeDTO = new SendChallengeDTO();
        sendChallengeDTO.setChallengerId(1L);
        sendChallengeDTO.setChallengedId(2L);
        sendChallengeDTO.setMaxRounds(3);
        sendChallengeDTO.setQuestionsPerRound(5);

        User challenger = createUser(1L, "challenger");
        User challenged = createUser(2L, "challenged");
        Challenge challenge = createChallenge(100L, challenger, challenged, ChallengeStatus.PENDING);

        when(challengeService.sendChallenge(sendChallengeDTO)).thenReturn(challenge);

        ResponseEntity<ChallengeDTO> response = controller.sendChallenge(sendChallengeDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(100L, response.getBody().getId());
        assertEquals(1L, response.getBody().getChallengerId());
        assertEquals(2L, response.getBody().getChallengedId());
        assertEquals(ChallengeStatus.PENDING, response.getBody().getStatus());
        assertEquals(3, response.getBody().getMaxRounds());
        assertEquals(5, response.getBody().getQuestionsPerRound());
        verify(challengeService).sendChallenge(sendChallengeDTO);
    }

    @Test
    void acceptChallenge_returnsAcceptedChallenge() {
        User challenger = createUser(1L, "challenger");
        User challenged = createUser(2L, "challenged");
        Challenge challenge = createChallenge(50L, challenger, challenged, ChallengeStatus.ACTIVE);

        when(challengeService.acceptChallenge(50L)).thenReturn(challenge);

        ResponseEntity<ChallengeDTO> response = controller.acceptChallenge(50L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(50L, response.getBody().getId());
        assertEquals(1L, response.getBody().getChallengerId());
        assertEquals(2L, response.getBody().getChallengedId());
        assertEquals(ChallengeStatus.ACTIVE, response.getBody().getStatus());
        verify(challengeService).acceptChallenge(50L);
    }

    @Test
    void acceptChallenge_withDifferentId_callsServiceCorrectly() {
        User challenger = createUser(3L, "challenger3");
        User challenged = createUser(4L, "challenged4");
        Challenge challenge = createChallenge(75L, challenger, challenged, ChallengeStatus.ACTIVE);

        when(challengeService.acceptChallenge(75L)).thenReturn(challenge);

        ResponseEntity<ChallengeDTO> response = controller.acceptChallenge(75L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(75L, response.getBody().getId());
        assertEquals(3L, response.getBody().getChallengerId());
        assertEquals(4L, response.getBody().getChallengedId());
        verify(challengeService).acceptChallenge(75L);
    }

    @Test
    void answerRound_returnsUpdatedChallenge() {
        AnswerDTO answerDTO = new AnswerDTO();
        answerDTO.setChallengeId(30L);
        answerDTO.setRoundNumber(1);
        answerDTO.setRol(ChallengeRol.CHALLENGER); // ✅ Esto ya está importado desde dto.*
        answerDTO.setScore(85);

        User challenger = createUser(1L, "challenger");
        User challenged = createUser(2L, "challenged");
        Challenge challenge = createChallenge(30L, challenger, challenged, ChallengeStatus.ACTIVE);

        when(challengeService.answerRound(answerDTO)).thenReturn(challenge);

        ResponseEntity<ChallengeDTO> response = controller.answerRound(answerDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(30L, response.getBody().getId());
        assertEquals(1L, response.getBody().getChallengerId());
        assertEquals(2L, response.getBody().getChallengedId());
        assertEquals(ChallengeStatus.ACTIVE, response.getBody().getStatus());
        verify(challengeService).answerRound(answerDTO);
    }

    @Test
    void answerRound_withDifferentData_returnsCorrectChallenge() {
        AnswerDTO answerDTO = new AnswerDTO();
        answerDTO.setChallengeId(40L);
        answerDTO.setRoundNumber(2);
        answerDTO.setRol(ChallengeRol.CHALLENGED); // ✅ Esto ya está importado desde dto.*
        answerDTO.setScore(92);

        User challenger = createUser(5L, "challenger5");
        User challenged = createUser(6L, "challenged6");
        Challenge challenge = createChallenge(40L, challenger, challenged, ChallengeStatus.COMPLETED);

        when(challengeService.answerRound(answerDTO)).thenReturn(challenge);

        ResponseEntity<ChallengeDTO> response = controller.answerRound(answerDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(40L, response.getBody().getId());
        assertEquals(5L, response.getBody().getChallengerId());
        assertEquals(6L, response.getBody().getChallengedId());
        assertEquals(ChallengeStatus.COMPLETED, response.getBody().getStatus());
        verify(challengeService).answerRound(answerDTO);
    }

    @Test
    void constructor_initializesController() {
        ChallengeController testController = new ChallengeController(challengeService);
        assertNotNull(testController);
    }

    @Test
    void getUsersToChallenge_withDifferentChallengerId_callsServiceCorrectly() {
        User user = createUser(10L, "testuser");
        List<User> users = Arrays.asList(user);

        when(challengeService.getUsersToChallenge(99L)).thenReturn(users);

        ResponseEntity<List<UserDTO>> response = controller.getUsersToChallenge(99L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(10L, response.getBody().get(0).getId());
        assertEquals("testuser", response.getBody().get(0).getUsername());
        verify(challengeService).getUsersToChallenge(99L);
    }

    @Test
    void getAll_withDifferentChallengeData_returnsCorrectMapping() {
        User challenger = createUser(7L, "challenger7");
        User challenged = createUser(8L, "challenged8");
        Challenge challenge = createChallenge(88L, challenger, challenged, ChallengeStatus.EXPIRED);
        challenge.setMaxRounds(5);
        challenge.setQuestionsPerRound(10);

        List<Challenge> challenges = Arrays.asList(challenge);

        when(challengeService.getAll()).thenReturn(challenges);

        ResponseEntity<List<ChallengeDTO>> response = controller.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(88L, response.getBody().get(0).getId());
        assertEquals(7L, response.getBody().get(0).getChallengerId());
        assertEquals(8L, response.getBody().get(0).getChallengedId());
        assertEquals(ChallengeStatus.EXPIRED, response.getBody().get(0).getStatus());
        assertEquals(5, response.getBody().get(0).getMaxRounds());
        assertEquals(10, response.getBody().get(0).getQuestionsPerRound());
        verify(challengeService).getAll();
    }

    // Helper methods
    private User createUser(Long id, String username) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(username + "@test.com");
        user.setName("Name" + id);
        user.setSurname("Surname" + id);
        user.setPoints(100);
        user.setCurrentLesson(1);
        return user;
    }

    private Challenge createChallenge(Long id, User challenger, User challenged, ChallengeStatus status) {
        Challenge challenge = new Challenge();
        challenge.setId(id);
        challenge.setChallenger(challenger);
        challenge.setChallenged(challenged);
        challenge.setStatus(status);
        challenge.setMaxRounds(3);
        challenge.setQuestionsPerRound(5);
        challenge.setCreationDate(LocalDate.now());
        challenge.setExpireDate(LocalDate.now().plusDays(7));
        challenge.setRounds(Arrays.asList());
        return challenge;
    }
}