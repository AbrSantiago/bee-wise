package com.beewise.model.challenge;

import com.beewise.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ChallengeTest {

    @Test
    void noArgsConstructor_createsEmptyObject() {
        Challenge challenge = new Challenge();

        assertNull(challenge.getId());
        assertNull(challenge.getChallenger());
        assertNull(challenge.getChallenged());
        assertEquals(ChallengeStatus.PENDING, challenge.getStatus());
        assertEquals(3, challenge.getMaxRounds());
        assertEquals(10, challenge.getQuestionsPerRound());
        assertNull(challenge.getCreationDate());
        assertNull(challenge.getExpireDate());
        assertNull(challenge.getResult());
        assertNotNull(challenge.getRounds());
        assertTrue(challenge.getRounds().isEmpty());
    }

    @Test
    void parameterizedConstructor_setsFieldsCorrectly() {
        User challenger = new User();
        challenger.setId(1L);
        User challenged = new User();
        challenged.setId(2L);

        Challenge challenge = new Challenge(challenger, challenged, 5, 15);

        assertEquals(challenger, challenge.getChallenger());
        assertEquals(challenged, challenge.getChallenged());
        assertEquals(5, challenge.getMaxRounds());
        assertEquals(15, challenge.getQuestionsPerRound());
        assertEquals(LocalDate.now(), challenge.getCreationDate());
        assertEquals(LocalDate.now().plusDays(3), challenge.getExpireDate());
        assertEquals(ChallengeStatus.PENDING, challenge.getStatus());
        assertNull(challenge.getResult());
    }

    @Test
    void setters_workCorrectly() {
        Challenge challenge = new Challenge();
        User challenger = new User();
        challenger.setId(10L);
        User challenged = new User();
        challenged.setId(20L);
        LocalDate creationDate = LocalDate.of(2025, 1, 1);
        LocalDate expireDate = LocalDate.of(2025, 2, 1);

        challenge.setId(100L);
        challenge.setChallenger(challenger);
        challenge.setChallenged(challenged);
        challenge.setStatus(ChallengeStatus.ACTIVE);
        challenge.setMaxRounds(7);
        challenge.setQuestionsPerRound(20);
        challenge.setCreationDate(creationDate);
        challenge.setExpireDate(expireDate);
        challenge.setResult(ChallengeResult.CHALLENGER_WIN);

        assertEquals(100L, challenge.getId());
        assertEquals(challenger, challenge.getChallenger());
        assertEquals(challenged, challenge.getChallenged());
        assertEquals(ChallengeStatus.ACTIVE, challenge.getStatus());
        assertEquals(7, challenge.getMaxRounds());
        assertEquals(20, challenge.getQuestionsPerRound());
        assertEquals(creationDate, challenge.getCreationDate());
        assertEquals(expireDate, challenge.getExpireDate());
        assertEquals(ChallengeResult.CHALLENGER_WIN, challenge.getResult());
    }

    @Test
    void setRounds_worksCorrectly() {
        Challenge challenge = new Challenge();
        Round round1 = new Round();
        Round round2 = new Round();
        ArrayList<Round> rounds = new ArrayList<>();
        rounds.add(round1);
        rounds.add(round2);

        challenge.setRounds(rounds);

        assertEquals(2, challenge.getRounds().size());
        assertEquals(round1, challenge.getRounds().get(0));
        assertEquals(round2, challenge.getRounds().get(1));
    }
}