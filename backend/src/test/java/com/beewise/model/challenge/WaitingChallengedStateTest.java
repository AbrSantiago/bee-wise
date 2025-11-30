package com.beewise.model.challenge;

import com.beewise.controller.dto.AnswerDTO;
import com.beewise.controller.dto.ChallengeRol;
import com.beewise.exception.AnswerWrongRolException;
import com.beewise.model.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class WaitingChallengedStateTest {

    @Test
    void answer_withCorrectRol_setsScore() {
        WaitingChallengedState state = new WaitingChallengedState();
        Challenge challenge = createChallenge();
        Round round = new Round(challenge, 1, RoundStatus.WAITING_CHALLENGED);
        AnswerDTO answer = new AnswerDTO();
        answer.setRol(ChallengeRol.CHALLENGED);
        answer.setScore(75);
        answer.setRoundNumber(1);

        state.answer(challenge, round, answer);

        assertEquals(75, round.getChallengedScore());
        assertEquals(RoundStatus.COMPLETED, round.getStatus());
    }

    @Test
    void answer_withWrongRol_throwsException() {
        WaitingChallengedState state = new WaitingChallengedState();
        Challenge challenge = createChallenge();
        Round round = new Round(challenge, 1, RoundStatus.WAITING_CHALLENGED);
        AnswerDTO answer = new AnswerDTO();
        answer.setRol(ChallengeRol.CHALLENGER);

        assertThrows(AnswerWrongRolException.class, () -> {
            state.answer(challenge, round, answer);
        });
    }

    @Test
    void answer_withOddRoundNumber_completesRound() {
        WaitingChallengedState state = new WaitingChallengedState();
        Challenge challenge = createChallenge();
        Round round = new Round(challenge, 1, RoundStatus.WAITING_CHALLENGED);
        AnswerDTO answer = new AnswerDTO();
        answer.setRol(ChallengeRol.CHALLENGED);
        answer.setScore(82);
        answer.setRoundNumber(1);

        state.answer(challenge, round, answer);

        assertEquals(82, round.getChallengedScore());
        assertEquals(RoundStatus.COMPLETED, round.getStatus());
    }

    @Test
    void answer_withEvenRoundNumber_switchesToWaitingChallenger() {
        WaitingChallengedState state = new WaitingChallengedState();
        Challenge challenge = createChallenge();
        Round round = new Round(challenge, 2, RoundStatus.WAITING_CHALLENGED);
        AnswerDTO answer = new AnswerDTO();
        answer.setRol(ChallengeRol.CHALLENGED);
        answer.setScore(78);
        answer.setRoundNumber(2);

        state.answer(challenge, round, answer);

        assertEquals(78, round.getChallengedScore());
        assertEquals(RoundStatus.WAITING_CHALLENGER, round.getStatus());
    }

    @Test
    void answer_withOddRoundAndNotMaxRounds_createsNextRound() {
        WaitingChallengedState state = new WaitingChallengedState();
        Challenge challenge = createChallenge();
        challenge.setMaxRounds(3);
        Round round = new Round(challenge, 1, RoundStatus.WAITING_CHALLENGED);
        AnswerDTO answer = new AnswerDTO();
        answer.setRol(ChallengeRol.CHALLENGED);
        answer.setScore(92);
        answer.setRoundNumber(1);

        int initialRounds = challenge.getRounds().size();
        state.answer(challenge, round, answer);

        assertEquals(initialRounds + 1, challenge.getRounds().size());
        Round nextRound = challenge.getRounds().get(challenge.getRounds().size() - 1);
        assertEquals(2, nextRound.getRoundNumber());
        assertEquals(RoundStatus.WAITING_CHALLENGED, nextRound.getStatus());
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