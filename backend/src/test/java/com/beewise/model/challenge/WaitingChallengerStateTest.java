package com.beewise.model.challenge;

import com.beewise.controller.dto.AnswerDTO;
import com.beewise.controller.dto.ChallengeRol;
import com.beewise.exception.AnswerWrongRolException;
import com.beewise.model.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class WaitingChallengerStateTest {

    @Test
    void answer_withCorrectRol_setsScore() {
        WaitingChallengerState state = new WaitingChallengerState();
        Challenge challenge = createChallenge();
        Round round = new Round(challenge, 1, RoundStatus.WAITING_CHALLENGER);
        AnswerDTO answer = new AnswerDTO();
        answer.setRol(ChallengeRol.CHALLENGER);
        answer.setScore(85);
        answer.setRoundNumber(1);

        state.answer(challenge, round, answer);

        assertEquals(85, round.getChallengerScore());
        assertEquals(RoundStatus.WAITING_CHALLENGED, round.getStatus());
    }

    @Test
    void answer_withWrongRol_throwsException() {
        WaitingChallengerState state = new WaitingChallengerState();
        Challenge challenge = createChallenge();
        Round round = new Round(challenge, 1, RoundStatus.WAITING_CHALLENGER);
        AnswerDTO answer = new AnswerDTO();
        answer.setRol(ChallengeRol.CHALLENGED);

        assertThrows(AnswerWrongRolException.class, () -> {
            state.answer(challenge, round, answer);
        });
    }

    @Test
    void answer_withEvenRoundNumber_completesRound() {
        WaitingChallengerState state = new WaitingChallengerState();
        Challenge challenge = createChallenge();
        Round round = new Round(challenge, 2, RoundStatus.WAITING_CHALLENGER);
        AnswerDTO answer = new AnswerDTO();
        answer.setRol(ChallengeRol.CHALLENGER);
        answer.setScore(90);
        answer.setRoundNumber(2);

        state.answer(challenge, round, answer);

        assertEquals(90, round.getChallengerScore());
        assertEquals(RoundStatus.COMPLETED, round.getStatus());
    }

    @Test
    void answer_withEvenRoundAndNotMaxRounds_createsNextRound() {
        WaitingChallengerState state = new WaitingChallengerState();
        Challenge challenge = createChallenge();
        challenge.setMaxRounds(3);
        Round round = new Round(challenge, 2, RoundStatus.WAITING_CHALLENGER);
        AnswerDTO answer = new AnswerDTO();
        answer.setRol(ChallengeRol.CHALLENGER);
        answer.setScore(95);
        answer.setRoundNumber(2);

        int initialRounds = challenge.getRounds().size();
        state.answer(challenge, round, answer);

        assertEquals(initialRounds + 1, challenge.getRounds().size());
        Round nextRound = challenge.getRounds().get(challenge.getRounds().size() - 1);
        assertEquals(3, nextRound.getRoundNumber());
        assertEquals(RoundStatus.WAITING_CHALLENGER, nextRound.getStatus());
    }

    @Test
    void answer_withMaxRounds_doesNotCreateNextRound() {
        WaitingChallengerState state = new WaitingChallengerState();
        Challenge challenge = createChallenge();
        challenge.setMaxRounds(2);
        Round round = new Round(challenge, 2, RoundStatus.WAITING_CHALLENGER);
        AnswerDTO answer = new AnswerDTO();
        answer.setRol(ChallengeRol.CHALLENGER);
        answer.setScore(88);
        answer.setRoundNumber(2);

        int initialRounds = challenge.getRounds().size();
        state.answer(challenge, round, answer);

        assertEquals(initialRounds, challenge.getRounds().size());
    }

    private Challenge createChallenge() {
        User challenger = new User();
        challenger.setId(1L);
        User challenged = new User();
        challenged.setId(2L);
        Challenge challenge = new Challenge(challenger, challenged, 3, 10);
        challenge.setRounds(new ArrayList<>());
        return challenge;
    }
}