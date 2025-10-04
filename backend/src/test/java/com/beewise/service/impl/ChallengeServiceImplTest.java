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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ChallengeServiceImplTest {

    @Autowired
    private ChallengeService challengeService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChallengeRepository challengeRepository;

    private User realChallenger;
    private User realChallenged;

    @BeforeEach
    void setUp() {
        List<User> allUsers = userRepository.findAll();

        assertTrue(allUsers.size() >= 2, "Necesitas al menos 2 usuarios reales en tu BD");

        realChallenger = allUsers.get(0);
        realChallenged = allUsers.get(1);
    }

    @Test
    void sendChallenge_withRealUsers_createsChallenge() {
        long challengesBefore = challengeRepository.count();

        SendChallengeDTO dto = new SendChallengeDTO();
        dto.setChallengerId(realChallenger.getId());
        dto.setChallengedId(realChallenged.getId());
        dto.setMaxRounds(3);
        dto.setQuestionsPerRound(5);

        Challenge result = challengeService.sendChallenge(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(realChallenger.getId(), result.getChallenger().getId());
        assertEquals(realChallenged.getId(), result.getChallenged().getId());
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
        dto.setChallengerId(realChallenger.getId());
        dto.setChallengedId(realChallenged.getId());
        dto.setMaxRounds(3);
        dto.setQuestionsPerRound(5);

        Challenge pendingChallenge = challengeService.sendChallenge(dto);
        assertEquals(ChallengeStatus.PENDING, pendingChallenge.getStatus());

        AnswerDTO challengerAnswer = new AnswerDTO();
        challengerAnswer.setChallengeId(pendingChallenge.getId());
        challengerAnswer.setRoundNumber(1);
        challengerAnswer.setRol(ChallengeRol.CHALLENGER);
        challengerAnswer.setScore(85);

        Challenge afterChallengerPlay = challengeService.answerRound(challengerAnswer);

        Challenge acceptedChallenge = challengeService.acceptChallenge(pendingChallenge.getId());

        assertNotNull(acceptedChallenge);
        assertEquals(ChallengeStatus.ACTIVE, acceptedChallenge.getStatus());
        assertEquals(pendingChallenge.getId(), acceptedChallenge.getId());

        Challenge challengeInDB = challengeRepository.findById(pendingChallenge.getId()).orElse(null);
        assertNotNull(challengeInDB);
        assertEquals(ChallengeStatus.ACTIVE, challengeInDB.getStatus());
    }

    @Test
    void answerRound_withActiveChallenge_updatesRound() {
        SendChallengeDTO dto = new SendChallengeDTO();
        dto.setChallengerId(realChallenger.getId());
        dto.setChallengedId(realChallenged.getId());
        dto.setMaxRounds(3);
        dto.setQuestionsPerRound(5);

        Challenge challenge = challengeService.sendChallenge(dto);

        AnswerDTO challengerAnswer = new AnswerDTO();
        challengerAnswer.setChallengeId(challenge.getId());
        challengerAnswer.setRoundNumber(1);
        challengerAnswer.setRol(ChallengeRol.CHALLENGER);
        challengerAnswer.setScore(85);

        Challenge afterChallengerAnswer = challengeService.answerRound(challengerAnswer);

        Challenge activeChallenge = challengeService.acceptChallenge(challenge.getId());
        assertEquals(ChallengeStatus.ACTIVE, activeChallenge.getStatus());

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
    void getAll_withRealData_returnsRealChallenges() {
        List<Challenge> allChallenges = challengeService.getAll();

        assertNotNull(allChallenges);

        SendChallengeDTO dto = new SendChallengeDTO();
        dto.setChallengerId(realChallenger.getId());
        dto.setChallengedId(realChallenged.getId());
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
        dto.setChallengerId(realChallenger.getId());
        dto.setChallengedId(realChallenger.getId());
        dto.setMaxRounds(3);
        dto.setQuestionsPerRound(5);

        UserChallengesHimselfException exception = assertThrows(
                UserChallengesHimselfException.class,
                () -> challengeService.sendChallenge(dto)
        );

        assertEquals("User " + realChallenger.getId() + " cannot challenge himself", exception.getMessage());
    }

    @Test
    void sendChallenge_challengeAlreadyExists_throwsException() {
        SendChallengeDTO firstDto = new SendChallengeDTO();
        firstDto.setChallengerId(realChallenger.getId());
        firstDto.setChallengedId(realChallenged.getId());
        firstDto.setMaxRounds(3);
        firstDto.setQuestionsPerRound(5);

        Challenge firstChallenge = challengeService.sendChallenge(firstDto);

        SendChallengeDTO secondDto = new SendChallengeDTO();
        secondDto.setChallengerId(realChallenger.getId());
        secondDto.setChallengedId(realChallenged.getId());
        secondDto.setMaxRounds(5);
        secondDto.setQuestionsPerRound(10);

        ChallengeAlreadyExistsException exception = assertThrows(
                ChallengeAlreadyExistsException.class,
                () -> challengeService.sendChallenge(secondDto)
        );

        assertEquals("Challenge between users " + realChallenger.getId() + " and " + realChallenged.getId() + " already exists", exception.getMessage());
    }

    @Test
    void getUsersToChallenge_withRealUsers_returnsRealUsers() {
        List<User> usersToChallenge = challengeService.getUsersToChallenge(realChallenger.getId());

        assertNotNull(usersToChallenge);

        boolean includesChallenger = usersToChallenge.stream()
                .anyMatch(u -> u.getId().equals(realChallenger.getId()));
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
    void exploreRealData_showsYourActualData() {
        List<User> users = userRepository.findAll();
        List<Challenge> challenges = challengeRepository.findAll();

        assertTrue(users.size() > 0, "Deberías tener al menos 1 usuario en tu BD");
    }
}