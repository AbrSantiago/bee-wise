package com.beewise.controller.dto;

import com.beewise.model.challenge.*;
import com.beewise.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChallengeDTOTest {

    @Test
    void constructor_mapsAllFieldsCorrectly() {

        User challenger = new User();
        challenger.setId(1L);

        User challenged = new User();
        challenged.setId(2L);

        Challenge challenge = new Challenge();
        challenge.setId(10L);
        challenge.setChallenger(challenger);
        challenge.setChallenged(challenged);
        challenge.setStatus(ChallengeStatus.PENDING);
        challenge.setMaxRounds(5);
        challenge.setQuestionsPerRound(3);
        challenge.setCreationDate(LocalDate.of(2025, 1, 1));
        challenge.setExpireDate(LocalDate.of(2025, 2, 1));
        challenge.setResult(ChallengeResult.DRAW);

        Round round = new Round(challenge, 1, RoundStatus.WAITING_CHALLENGER);
        challenge.setRounds(List.of(round));

        ChallengeDTO dto = new ChallengeDTO(challenge);

        assertEquals(10L, dto.getId());
        assertEquals(1L, dto.getChallengerId());
        assertEquals(2L, dto.getChallengedId());
        assertEquals(ChallengeStatus.PENDING, dto.getStatus());
        assertEquals(1, dto.getRounds().size());
        assertEquals(5, dto.getMaxRounds());
        assertEquals(3, dto.getQuestionsPerRound());
        assertEquals(LocalDate.of(2025, 1, 1), dto.getCreationDate());
        assertEquals(LocalDate.of(2025, 2, 1), dto.getExpireDate());
        assertEquals(ChallengeResult.DRAW, dto.getResult());
    }

    @Test
    void constructor_withMultipleRounds_mapsAllRounds() {

        User challenger = new User();
        challenger.setId(15L);

        User challenged = new User();
        challenged.setId(25L);

        Challenge challenge = new Challenge();
        challenge.setId(30L);
        challenge.setChallenger(challenger);
        challenge.setChallenged(challenged);
        challenge.setStatus(ChallengeStatus.ACTIVE);
        challenge.setMaxRounds(4);
        challenge.setQuestionsPerRound(6);
        challenge.setCreationDate(LocalDate.of(2025, 7, 1));
        challenge.setExpireDate(LocalDate.of(2025, 8, 1));
        challenge.setResult(null);

        Round round1 = new Round(challenge, 1, RoundStatus.WAITING_CHALLENGER);
        Round round2 = new Round(challenge, 2, RoundStatus.COMPLETED);
        challenge.setRounds(List.of(round1, round2));

        ChallengeDTO dto = new ChallengeDTO(challenge);

        assertEquals(30L, dto.getId());
        assertEquals(15L, dto.getChallengerId());
        assertEquals(25L, dto.getChallengedId());
        assertEquals(ChallengeStatus.ACTIVE, dto.getStatus());
        assertEquals(2, dto.getRounds().size());
        assertEquals(4, dto.getMaxRounds());
        assertEquals(6, dto.getQuestionsPerRound());
        assertEquals(LocalDate.of(2025, 7, 1), dto.getCreationDate());
        assertEquals(LocalDate.of(2025, 8, 1), dto.getExpireDate());
        assertNull(dto.getResult());
    }

    @Test
    void fromChallenge_withNullFields_handlesCorrectly() {

        User challenger = new User();
        challenger.setId(100L);

        User challenged = new User();
        challenged.setId(200L);

        Challenge challenge = new Challenge();
        challenge.setId(null);
        challenge.setChallenger(challenger);
        challenge.setChallenged(challenged);
        challenge.setStatus(null);
        challenge.setRounds(List.of());
        challenge.setMaxRounds(0);
        challenge.setQuestionsPerRound(0);
        challenge.setCreationDate(null);
        challenge.setExpireDate(null);
        challenge.setResult(null);

        ChallengeDTO dto = ChallengeDTO.fromChallenge(challenge);

        assertNull(dto.getId());
        assertEquals(100L, dto.getChallengerId());
        assertEquals(200L, dto.getChallengedId());
        assertNull(dto.getStatus());
        assertTrue(dto.getRounds().isEmpty());
        assertEquals(0, dto.getMaxRounds());
        assertEquals(0, dto.getQuestionsPerRound());
        assertNull(dto.getCreationDate());
        assertNull(dto.getExpireDate());
        assertNull(dto.getResult());
    }
}