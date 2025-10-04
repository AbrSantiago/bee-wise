package com.beewise.service.impl;

import com.beewise.controller.dto.AnswerDTO;
import com.beewise.controller.dto.ChallengeRol;
import com.beewise.controller.dto.SendChallengeDTO;
import com.beewise.exception.*;
import com.beewise.model.User;
import com.beewise.model.challenge.*;
import com.beewise.repository.ChallengeRepository;
import com.beewise.repository.UserRepository;
import com.beewise.service.ChallengeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles({"ci"})
class ChallengeServiceImplTest {

    @Autowired
    private ChallengeService challengeService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChallengeRepository challengeRepository;

    private User challenger;
    private User challenged;

    @BeforeEach
    void setUp() {
        List<User> existingUsers = userRepository.findAll();

        // Si hay usuarios existentes (desarrollo local), usarlos
        if (existingUsers.size() >= 2) {
            challenger = existingUsers.get(0);
            challenged = existingUsers.get(1);
        } else {
            // Si no hay usuarios (CI), crear usuarios de prueba
            challenger = createTestUser("testChallenger", "challenger@test.com");
            challenged = createTestUser("testChallenged", "challenged@test.com");
        }
    }

    private User createTestUser(String username, String email) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setName("Test");
        user.setSurname("User");
        user.setPasswordHash("$2a$10$N9qo8uLOickgx2ZMRZoMye");
        user.setPoints(100);
        user.setCurrentLesson(1);
        return userRepository.save(user);
    }

    @Test
    void sendChallenge_withValidUsers_createsChallenge() {
        long challengesBefore = challengeRepository.count();

        SendChallengeDTO dto = new SendChallengeDTO();
        dto.setChallengerId(challenger.getId());
        dto.setChallengedId(challenged.getId());
        dto.setMaxRounds(3);
        dto.setQuestionsPerRound(5);

        Challenge result = challengeService.sendChallenge(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(challenger.getId(), result.getChallenger().getId());
        assertEquals(challenged.getId(), result.getChallenged().getId());
        assertEquals(ChallengeStatus.PENDING, result.getStatus());
        assertEquals(3, result.getMaxRounds());
        assertEquals(5, result.getQuestionsPerRound());

        long challengesAfter = challengeRepository.count();
        assertEquals(challengesBefore + 1, challengesAfter);

        Optional<Challenge> savedChallenge = challengeRepository.findById(result.getId());
        assertTrue(savedChallenge.isPresent());
        assertEquals(result.getId(), savedChallenge.get().getId());
    }

    @Test
    void acceptChallenge_followingCorrectFlow_updatesStatus() {
        SendChallengeDTO dto = new SendChallengeDTO();
        dto.setChallengerId(challenger.getId());
        dto.setChallengedId(challenged.getId());
        dto.setMaxRounds(3);
        dto.setQuestionsPerRound(5);

        Challenge pendingChallenge = challengeService.sendChallenge(dto);
        assertEquals(ChallengeStatus.PENDING, pendingChallenge.getStatus());

        // Challenger debe jugar primero
        AnswerDTO challengerAnswer = new AnswerDTO();
        challengerAnswer.setChallengeId(pendingChallenge.getId());
        challengerAnswer.setRoundNumber(1);
        challengerAnswer.setRol(ChallengeRol.CHALLENGER);
        challengerAnswer.setScore(85);

        challengeService.answerRound(challengerAnswer);
        Challenge acceptedChallenge = challengeService.acceptChallenge(pendingChallenge.getId());

        assertNotNull(acceptedChallenge);
        assertEquals(ChallengeStatus.ACTIVE, acceptedChallenge.getStatus());
        assertEquals(pendingChallenge.getId(), acceptedChallenge.getId());
    }

    @Test
    void answerRound_withActiveChallenge_updatesRound() {
        SendChallengeDTO dto = new SendChallengeDTO();
        dto.setChallengerId(challenger.getId());
        dto.setChallengedId(challenged.getId());
        dto.setMaxRounds(3);
        dto.setQuestionsPerRound(5);

        Challenge challenge = challengeService.sendChallenge(dto);

        // Challenger juega primero
        AnswerDTO challengerAnswer = new AnswerDTO();
        challengerAnswer.setChallengeId(challenge.getId());
        challengerAnswer.setRoundNumber(1);
        challengerAnswer.setRol(ChallengeRol.CHALLENGER);
        challengerAnswer.setScore(85);

        challengeService.answerRound(challengerAnswer);
        Challenge activeChallenge = challengeService.acceptChallenge(challenge.getId());

        // Challenged responde
        AnswerDTO challengedAnswer = new AnswerDTO();
        challengedAnswer.setChallengeId(activeChallenge.getId());
        challengedAnswer.setRoundNumber(1);
        challengedAnswer.setRol(ChallengeRol.CHALLENGED);
        challengedAnswer.setScore(90);

        Challenge updatedChallenge = challengeService.answerRound(challengedAnswer);

        assertNotNull(updatedChallenge);
        assertEquals(activeChallenge.getId(), updatedChallenge.getId());
    }

    @Test
    void getAll_returnsAllChallenges() {
        List<Challenge> allChallenges = challengeService.getAll();
        assertNotNull(allChallenges);

        SendChallengeDTO dto = new SendChallengeDTO();
        dto.setChallengerId(challenger.getId());
        dto.setChallengedId(challenged.getId());
        dto.setMaxRounds(2);
        dto.setQuestionsPerRound(5);

        Challenge newChallenge = challengeService.sendChallenge(dto);

        List<Challenge> updatedList = challengeService.getAll();
        assertEquals(allChallenges.size() + 1, updatedList.size());

        boolean ourChallengeExists = updatedList.stream()
                .anyMatch(c -> c.getId().equals(newChallenge.getId()));
        assertTrue(ourChallengeExists);
    }

    @Test
    void sendChallenge_userChallengesHimself_throwsException() {
        SendChallengeDTO dto = new SendChallengeDTO();
        dto.setChallengerId(challenger.getId());
        dto.setChallengedId(challenger.getId());
        dto.setMaxRounds(3);
        dto.setQuestionsPerRound(5);

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
        dto.setMaxRounds(3);
        dto.setQuestionsPerRound(5);

        challengeService.sendChallenge(dto);

        SendChallengeDTO secondDto = new SendChallengeDTO();
        secondDto.setChallengerId(challenger.getId());
        secondDto.setChallengedId(challenged.getId());
        secondDto.setMaxRounds(5);
        secondDto.setQuestionsPerRound(10);

        ChallengeAlreadyExistsException exception = assertThrows(
                ChallengeAlreadyExistsException.class,
                () -> challengeService.sendChallenge(secondDto)
        );

        assertEquals("Challenge between users " + challenger.getId() + " and " + challenged.getId() + " already exists", exception.getMessage());
    }

    @Test
    void getUsersToChallenge_returnsAvailableUsers() {
        List<User> usersToChallenge = challengeService.getUsersToChallenge(challenger.getId());

        assertNotNull(usersToChallenge);

        boolean includesChallenger = usersToChallenge.stream()
                .anyMatch(u -> u.getId().equals(challenger.getId()));
        assertFalse(includesChallenger, "No debería incluir al usuario que hace el challenge");
    }

    @Test
    void acceptChallenge_challengeNotFound_throwsException() {
        Long nonExistentId = 999999L;

        ChallengeNotFoundException exception = assertThrows(
                ChallengeNotFoundException.class,
                () -> challengeService.acceptChallenge(nonExistentId)
        );

        assertEquals("Challenge with id " + nonExistentId + " does not exists", exception.getMessage());
    }

    @Test
    void answerRound_challengeNotFound_throwsException() {
        AnswerDTO answerDTO = new AnswerDTO();
        answerDTO.setChallengeId(999999L);
        answerDTO.setRoundNumber(1);
        answerDTO.setRol(ChallengeRol.CHALLENGER);
        answerDTO.setScore(85);

        ChallengeNotFoundException exception = assertThrows(
                ChallengeNotFoundException.class,
                () -> challengeService.answerRound(answerDTO)
        );

        assertEquals("Challenge with id 999999 does not exists", exception.getMessage());
    }

    @Test
    void workWithBothEnvironments_showsFlexibility() {
        // Este test demuestra que funciona tanto en CI (H2) como localmente (datos reales)
        List<User> users = userRepository.findAll();
        List<Challenge> challenges = challengeRepository.findAll();

        assertTrue(users.size() >= 2, "Debería tener al menos 2 usuarios (creados o existentes)");
        assertNotNull(challenges, "Debería poder consultar challenges");
    }
}