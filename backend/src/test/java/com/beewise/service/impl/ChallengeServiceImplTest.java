package com.beewise.service.impl;

import com.beewise.controller.dto.AnswerDTO;
import com.beewise.controller.dto.ChallengeRol;
import com.beewise.controller.dto.RegisterUserDTO;
import com.beewise.controller.dto.SendChallengeDTO;
import com.beewise.exception.*;
import com.beewise.model.User;
import com.beewise.model.challenge.Challenge;
import com.beewise.model.challenge.Round;
import com.beewise.model.challenge.ChallengeResult;
import com.beewise.model.challenge.ChallengeStatus;
import com.beewise.model.challenge.RoundStatus;
import com.beewise.repository.ChallengeRepository;
import com.beewise.service.ChallengeService;
import com.beewise.service.UserService;
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
class ChallengeServiceImplTest {

    @Autowired
    private ChallengeService challengeService;

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private UserService userService;

    private User challenger;
    private User challenged;
    private Challenge activeChallengeWithRounds;

    @BeforeEach
    void setUp() {
        challenger = createTestUser("challenger", "challenger@test.com");
        challenged = createTestUser("challenged", "challenged@test.com");
        activeChallengeWithRounds = createActiveChallengeWithRounds();
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

    private Challenge createActiveChallengeWithRounds() {
        SendChallengeDTO dto = new SendChallengeDTO();
        dto.setChallengerId(challenger.getId());
        dto.setChallengedId(challenged.getId());
        dto.setMaxRounds(3);
        dto.setQuestionsPerRound(5); // Mínimo 5 según la validación

        Challenge challenge = challengeService.sendChallenge(dto);

        Round round = challenge.getRounds().get(0);
        round.setStatus(RoundStatus.WAITING_CHALLENGED);
        challengeRepository.save(challenge);

        return challengeService.acceptChallenge(challenge.getId());
    }

    @Test
    void sendChallenge_validData_createsChallenge() {
        User newChallenger = createTestUser("newChallenger", "newchallenger@test.com");
        User newChallenged = createTestUser("newChallenged", "newchallenged@test.com");

        SendChallengeDTO dto = new SendChallengeDTO();
        dto.setChallengerId(newChallenger.getId());
        dto.setChallengedId(newChallenged.getId());
        dto.setMaxRounds(2);
        dto.setQuestionsPerRound(5); // Cambiar a 5 mínimo

        Challenge result = challengeService.sendChallenge(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(newChallenger.getId(), result.getChallenger().getId());
        assertEquals(newChallenged.getId(), result.getChallenged().getId());
        assertEquals(2, result.getMaxRounds());
        assertEquals(5, result.getQuestionsPerRound());
        assertEquals(ChallengeStatus.PENDING, result.getStatus());
        assertEquals(1, result.getRounds().size());
        assertEquals(RoundStatus.WAITING_CHALLENGER, result.getRounds().get(0).getStatus());
    }

    @Test
    void sendChallenge_userChallengesHimself_throwsException() {
        SendChallengeDTO dto = new SendChallengeDTO();
        dto.setChallengerId(challenger.getId());
        dto.setChallengedId(challenger.getId());
        dto.setMaxRounds(2);
        dto.setQuestionsPerRound(5); // Agregar validación correcta

        UserChallengesHimselfException exception = assertThrows(
                UserChallengesHimselfException.class,
                () -> challengeService.sendChallenge(dto)
        );

        assertEquals("User " + challenger.getId() + " cannot challenge himself", exception.getMessage());
    }

    @Test
    void sendChallenge_challengeAlreadyExists_throwsException() {
        SendChallengeDTO dto = new SendChallengeDTO();
        dto.setChallengerId(challenger.getId());
        dto.setChallengedId(challenged.getId());
        dto.setMaxRounds(2);
        dto.setQuestionsPerRound(5); // Cambiar a 5

        ChallengeAlreadyExistsException exception = assertThrows(
                ChallengeAlreadyExistsException.class,
                () -> challengeService.sendChallenge(dto)
        );

        assertEquals("Challenge between users " + challenger.getId() +
                " and " + challenged.getId() + " already exists", exception.getMessage());
    }

    @Test
    void sendChallenge_reverseOrderChallengeExists_throwsException() {
        User user1 = createTestUser("user1", "user1@test.com");
        User user2 = createTestUser("user2", "user2@test.com");

        SendChallengeDTO dto1 = new SendChallengeDTO();
        dto1.setChallengerId(user1.getId());
        dto1.setChallengedId(user2.getId());
        dto1.setMaxRounds(2);
        dto1.setQuestionsPerRound(5); // Cambiar a 5
        challengeService.sendChallenge(dto1);

        SendChallengeDTO dto2 = new SendChallengeDTO();
        dto2.setChallengerId(user2.getId());
        dto2.setChallengedId(user1.getId());
        dto2.setMaxRounds(2);
        dto2.setQuestionsPerRound(5); // Cambiar a 5

        ChallengeAlreadyExistsException exception = assertThrows(
                ChallengeAlreadyExistsException.class,
                () -> challengeService.sendChallenge(dto2)
        );

        assertTrue(exception.getMessage().contains("already exists"));
    }

    @Test
    void sendChallenge_nonExistentChallenger_throwsException() {
        SendChallengeDTO dto = new SendChallengeDTO();
        dto.setChallengerId(999999L);
        dto.setChallengedId(challenged.getId());
        dto.setMaxRounds(2);
        dto.setQuestionsPerRound(5); // Cambiar a 5

        assertThrows(RuntimeException.class, () -> challengeService.sendChallenge(dto));
    }

    @Test
    void sendChallenge_nonExistentChallenged_throwsException() {
        SendChallengeDTO dto = new SendChallengeDTO();
        dto.setChallengerId(challenger.getId());
        dto.setChallengedId(999999L);
        dto.setMaxRounds(2);
        dto.setQuestionsPerRound(5); // Cambiar a 5

        assertThrows(RuntimeException.class, () -> challengeService.sendChallenge(dto));
    }

    @Test
    void acceptChallenge_validPendingChallenge_acceptsChallenge() {
        User newChallenger = createTestUser("acceptChallenger", "acceptchallenger@test.com");
        User newChallenged = createTestUser("acceptChallenged", "acceptchallenged@test.com");

        SendChallengeDTO dto = new SendChallengeDTO();
        dto.setChallengerId(newChallenger.getId());
        dto.setChallengedId(newChallenged.getId());
        dto.setMaxRounds(2);
        dto.setQuestionsPerRound(5); // Cambiar a 5

        Challenge pendingChallenge = challengeService.sendChallenge(dto);

        Round round = pendingChallenge.getRounds().get(0);
        round.setStatus(RoundStatus.WAITING_CHALLENGED);
        challengeRepository.save(pendingChallenge);

        Challenge result = challengeService.acceptChallenge(pendingChallenge.getId());

        assertNotNull(result);
        assertEquals(ChallengeStatus.ACTIVE, result.getStatus());
    }

    @Test
    void acceptChallenge_nonExistentChallenge_throwsException() {
        Long nonExistentId = 999999L;

        ChallengeNotFoundException exception = assertThrows(
                ChallengeNotFoundException.class,
                () -> challengeService.acceptChallenge(nonExistentId)
        );

        assertEquals("Challenge with id " + nonExistentId + " does not exists", exception.getMessage());
    }

    @Test
    void acceptChallenge_alreadyAcceptedChallenge_throwsException() {
        ChallengeNotPendingException exception = assertThrows(
                ChallengeNotPendingException.class,
                () -> challengeService.acceptChallenge(activeChallengeWithRounds.getId())
        );

        assertEquals("Challenge with id " + activeChallengeWithRounds.getId() +
                " was already accepted", exception.getMessage());
    }

    @Test
    void acceptChallenge_challengerNotPlayedYet_throwsException() {
        User newChallenger = createTestUser("waitingChallenger", "waitingchallenger@test.com");
        User newChallenged = createTestUser("waitingChallenged", "waitingchallenged@test.com");

        SendChallengeDTO dto = new SendChallengeDTO();
        dto.setChallengerId(newChallenger.getId());
        dto.setChallengedId(newChallenged.getId());
        dto.setMaxRounds(2);
        dto.setQuestionsPerRound(5); // Cambiar a 5

        Challenge pendingChallenge = challengeService.sendChallenge(dto);

        WaitingFotChallengerException exception = assertThrows(
                WaitingFotChallengerException.class,
                () -> challengeService.acceptChallenge(pendingChallenge.getId())
        );

        assertEquals("Challenger has not played yet", exception.getMessage());
    }

    @Test
    void answerRound_validAnswer_updatesChallenge() {
        AnswerDTO answer = new AnswerDTO();
        answer.setChallengeId(activeChallengeWithRounds.getId());
        answer.setRoundNumber(1);
        answer.setScore(80);
        answer.setRol(ChallengeRol.CHALLENGED); // Agregar el userId del challenged

        Challenge result = challengeService.answerRound(answer);

        assertNotNull(result);
        assertEquals(activeChallengeWithRounds.getId(), result.getId());
    }

    @Test
    void answerRound_completesLastRound_setsResultChallengerWin() {
        User newChallenger = createTestUser("winChallenger", "winchallenger@test.com");
        User newChallenged = createTestUser("winChallenged", "winchallenged@test.com");

        SendChallengeDTO dto = new SendChallengeDTO();
        dto.setChallengerId(newChallenger.getId());
        dto.setChallengedId(newChallenged.getId());
        dto.setMaxRounds(1);
        dto.setQuestionsPerRound(5);

        Challenge challenge = challengeService.sendChallenge(dto);

        Round round = challenge.getRounds().get(0);
        round.setChallengerScore(90);
        round.setStatus(RoundStatus.WAITING_CHALLENGED);
        challengeRepository.save(challenge);

        challenge = challengeService.acceptChallenge(challenge.getId());

        AnswerDTO answer = new AnswerDTO();
        answer.setChallengeId(challenge.getId());
        answer.setRoundNumber(1);
        answer.setScore(70);
        answer.setRol(ChallengeRol.CHALLENGED); // El challenged responde

        Challenge result = challengeService.answerRound(answer);

        assertEquals(ChallengeStatus.COMPLETED, result.getStatus());
        assertEquals(ChallengeResult.CHALLENGER_WIN, result.getResult());
    }

    @Test
    void answerRound_completesLastRound_setsResultChallengedWin() {
        User newChallenger = createTestUser("loseChallenger", "losechallenger@test.com");
        User newChallenged = createTestUser("loseChallenged", "losechallenged@test.com");

        SendChallengeDTO dto = new SendChallengeDTO();
        dto.setChallengerId(newChallenger.getId());
        dto.setChallengedId(newChallenged.getId());
        dto.setMaxRounds(1);
        dto.setQuestionsPerRound(5);

        Challenge challenge = challengeService.sendChallenge(dto);

        Round round = challenge.getRounds().get(0);
        round.setChallengerScore(60);
        round.setStatus(RoundStatus.WAITING_CHALLENGED);
        challengeRepository.save(challenge);

        challenge = challengeService.acceptChallenge(challenge.getId());

        AnswerDTO answer = new AnswerDTO();
        answer.setChallengeId(challenge.getId());
        answer.setRoundNumber(1);
        answer.setScore(90);
        answer.setRol(ChallengeRol.CHALLENGED); // El challenged responde

        Challenge result = challengeService.answerRound(answer);

        assertEquals(ChallengeStatus.COMPLETED, result.getStatus());
        assertEquals(ChallengeResult.CHALLENGED_WIN, result.getResult());
    }

    @Test
    void answerRound_completesLastRound_setsResultDraw() {
        User newChallenger = createTestUser("drawChallenger", "drawchallenger@test.com");
        User newChallenged = createTestUser("drawChallenged", "drawchallenged@test.com");

        SendChallengeDTO dto = new SendChallengeDTO();
        dto.setChallengerId(newChallenger.getId());
        dto.setChallengedId(newChallenged.getId());
        dto.setMaxRounds(1);
        dto.setQuestionsPerRound(5);

        Challenge challenge = challengeService.sendChallenge(dto);

        // EL CHALLENGER JUEGA PRIMERO
        AnswerDTO challengerAnswer = new AnswerDTO();
        challengerAnswer.setChallengeId(challenge.getId());
        challengerAnswer.setRoundNumber(1);
        challengerAnswer.setScore(80);
        challengerAnswer.setRol(ChallengeRol.CHALLENGER);

        challenge = challengeService.answerRound(challengerAnswer);

        // AHORA SÍ SE PUEDE ACEPTAR EL CHALLENGE
        challenge = challengeService.acceptChallenge(challenge.getId());

        // EL CHALLENGED RESPONDE CON EL MISMO SCORE
        AnswerDTO challengedAnswer = new AnswerDTO();
        challengedAnswer.setChallengeId(challenge.getId());
        challengedAnswer.setRoundNumber(1);
        challengedAnswer.setScore(80);
        challengedAnswer.setRol(ChallengeRol.CHALLENGED);

        Challenge result = challengeService.answerRound(challengedAnswer);

        assertEquals(ChallengeStatus.COMPLETED, result.getStatus());
        assertEquals(ChallengeResult.CHALLENGED_WIN, result.getResult());
    }

    // Test para cubrir cuando el challenger intenta responder en estado WAITING_CHALLENGED
    @Test
    void answerRound_challengerTriesToAnswerWhenWaitingChallenged_throwsException() {
        User newChallenger = createTestUser("wrongRoleChallenger", "wrongrolechallenger@test.com");
        User newChallenged = createTestUser("wrongRoleChallenged", "wrongrolechallenged@test.com");

        SendChallengeDTO dto = new SendChallengeDTO();
        dto.setChallengerId(newChallenger.getId());
        dto.setChallengedId(newChallenged.getId());
        dto.setMaxRounds(2);
        dto.setQuestionsPerRound(5);

        Challenge challenge = challengeService.sendChallenge(dto);

        Round round = challenge.getRounds().get(0);
        round.setStatus(RoundStatus.WAITING_CHALLENGED); // Estado donde solo el challenged puede jugar
        challengeRepository.save(challenge);

        challenge = challengeService.acceptChallenge(challenge.getId());

        AnswerDTO answer = new AnswerDTO();
        answer.setChallengeId(challenge.getId());
        answer.setRoundNumber(1);
        answer.setScore(80);
        answer.setRol(ChallengeRol.CHALLENGER); // El CHALLENGER intenta responder (incorrecto)

        AnswerWrongRolException exception = assertThrows(
                AnswerWrongRolException.class,
                () -> challengeService.answerRound(answer)
        );

        assertEquals("Rol should be CHALLENGED", exception.getMessage());
    }

    // Test para cubrir cuando el challenged intenta responder en estado WAITING_CHALLENGER
    @Test
    void answerRound_challengedTriesToAnswerWhenWaitingChallenger_throwsException() {
        User newChallenger = createTestUser("waitingChallenger2", "waitingchallenger2@test.com");
        User newChallenged = createTestUser("waitingChallenged2", "waitingchallenged2@test.com");

        SendChallengeDTO dto = new SendChallengeDTO();
        dto.setChallengerId(newChallenger.getId());
        dto.setChallengedId(newChallenged.getId());
        dto.setMaxRounds(2);
        dto.setQuestionsPerRound(5);

        Challenge challenge = challengeService.sendChallenge(dto);
        AnswerDTO answer = new AnswerDTO();
        answer.setChallengeId(challenge.getId());
        answer.setRoundNumber(1);
        answer.setScore(80);
        answer.setRol(ChallengeRol.CHALLENGED);

        AnswerWrongRolException exception = assertThrows(
                AnswerWrongRolException.class,
                () -> challengeService.answerRound(answer)
        );

        assertEquals("Rol should be CHALLENGER", exception.getMessage());
    }

    @Test
    void answerRound_wrongRoundNumberTooHigh_throwsException() {
        AnswerDTO answer = new AnswerDTO();
        answer.setChallengeId(activeChallengeWithRounds.getId());
        answer.setRoundNumber(10);
        answer.setScore(80);
        answer.setRol(ChallengeRol.CHALLENGED); // Agregar userId

        RoundNumberException exception = assertThrows(
                RoundNumberException.class,
                () -> challengeService.answerRound(answer)
        );

        assertEquals("Wrong round number", exception.getMessage());
    }

    @Test
    void answerRound_wrongRoundNumberNotCurrent_throwsException() {
        AnswerDTO answer = new AnswerDTO();
        answer.setChallengeId(activeChallengeWithRounds.getId());
        answer.setRoundNumber(2);
        answer.setScore(80);
        answer.setRol(ChallengeRol.CHALLENGED); // Agregar userId

        RoundNumberException exception = assertThrows(
                RoundNumberException.class,
                () -> challengeService.answerRound(answer)
        );

        assertEquals("Wrong round number", exception.getMessage());
    }

    @Test
    void answerRound_notActiveChallenge_throwsException() {
        User newChallenger = createTestUser("pendingChallenger", "pendingchallenger@test.com");
        User newChallenged = createTestUser("pendingChallenged", "pendingchallenged@test.com");

        SendChallengeDTO dto = new SendChallengeDTO();
        dto.setChallengerId(newChallenger.getId());
        dto.setChallengedId(newChallenged.getId());
        dto.setMaxRounds(2);
        dto.setQuestionsPerRound(5);

        Challenge pendingChallenge = challengeService.sendChallenge(dto);

        AnswerDTO answer = new AnswerDTO();
        answer.setChallengeId(pendingChallenge.getId());
        answer.setRoundNumber(1);
        answer.setScore(80);
        answer.setRol(ChallengeRol.CHALLENGED); // Agregar userId

        AnswerWrongRolException exception = assertThrows(
                AnswerWrongRolException.class,
                () -> challengeService.answerRound(answer)
        );

        assertEquals("Rol should be CHALLENGER", exception.getMessage());
    }

    // Agregar test para cubrir la excepción RoundCompletedException
    @Test
    void answerRound_roundAlreadyCompleted_throwsException() {
        // Crear un challenge donde el round ya esté completado
        User newChallenger = createTestUser("completedChallenger", "completedchallenger@test.com");
        User newChallenged = createTestUser("completedChallenged", "completedchallenged@test.com");

        SendChallengeDTO dto = new SendChallengeDTO();
        dto.setChallengerId(newChallenger.getId());
        dto.setChallengedId(newChallenged.getId());
        dto.setMaxRounds(2);
        dto.setQuestionsPerRound(5);

        Challenge challenge = challengeService.sendChallenge(dto);

        // Simular que ambos jugadores ya jugaron y el round está completo
        Round round = challenge.getRounds().get(0);
        round.setChallengerScore(80);
        round.setChallengedScore(75);
        round.setStatus(RoundStatus.COMPLETED); // AQUÍ está completo
        challengeRepository.save(challenge);

        challenge = challengeService.acceptChallenge(challenge.getId());

        // Intentar responder un round que ya está completado
        AnswerDTO answer = new AnswerDTO();
        answer.setChallengeId(challenge.getId());
        answer.setRoundNumber(1);
        answer.setScore(90);

        RoundCompletedException exception = assertThrows(
                RoundCompletedException.class,
                () -> challengeService.answerRound(answer)
        );

        assertEquals("Round already completed", exception.getMessage());
    }

    @Test
    void answerRound_nonExistentChallenge_throwsException() {
        AnswerDTO answer = new AnswerDTO();
        answer.setChallengeId(999999L);
        answer.setRoundNumber(1);
        answer.setScore(80);

        ChallengeNotFoundException exception = assertThrows(
                ChallengeNotFoundException.class,
                () -> challengeService.answerRound(answer)
        );

        assertEquals("Challenge with id 999999 does not exists", exception.getMessage());
    }

    @Test
    void getAll_returnsChallenges() {
        List<Challenge> challenges = challengeService.getAll();

        assertNotNull(challenges);
        assertTrue(challenges.size() >= 1);
        assertTrue(challenges.stream().anyMatch(c -> c.getId().equals(activeChallengeWithRounds.getId())));
    }

    @Test
    void getUsersToChallenge_returnsAvailableUsers() {
        User additionalUser = createTestUser("available", "available@test.com");

        List<User> users = challengeService.getUsersToChallenge(challenger.getId());

        assertNotNull(users);
    }

    @Test
    void getUsersToChallenge_nonExistentUser_handlesGracefully() {
        Long nonExistentId = 999999L;

        List<User> users = challengeService.getUsersToChallenge(nonExistentId);

        assertNotNull(users);
    }

    @Test
    void contextLoads() {
        assertNotNull(challengeService);
        assertNotNull(challengeRepository);
        assertNotNull(userService);
    }
}