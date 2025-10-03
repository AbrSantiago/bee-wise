package com.beewise.model.challenge;

import com.beewise.controller.dto.AnswerDTO;
import com.beewise.controller.dto.ChallengeRol;
import com.beewise.exception.RoundCompletedException;
import com.beewise.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoundTest {

    @Test
    void noArgsConstructor_createsEmptyObject() {
        Round round = new Round();

        assertNull(round.getId());
        assertNull(round.getChallenge());
        assertEquals(0, round.getRoundNumber());
        assertEquals(0, round.getChallengerScore());
        assertEquals(0, round.getChallengedScore());
        assertNull(round.getStatus());
    }

    @Test
    void parameterizedConstructor_setsFieldsCorrectly() {
        Challenge challenge = createChallenge();

        Round round = new Round(challenge, 3, RoundStatus.WAITING_CHALLENGER);

        assertEquals(challenge, round.getChallenge());
        assertEquals(3, round.getRoundNumber());
        assertEquals(RoundStatus.WAITING_CHALLENGER, round.getStatus());
        assertEquals(0, round.getChallengerScore());
        assertEquals(0, round.getChallengedScore());
    }

    @Test
    void winner_whenChallengerScoreHigher_returnsChallengerUser() {
        Challenge challenge = createChallenge();
        Round round = new Round(challenge, 1, RoundStatus.COMPLETED);
        round.setChallengerScore(85);
        round.setChallengedScore(70);

        User winner = round.winner();

        assertEquals(challenge.getChallenger(), winner);
    }

    @Test
    void winner_whenChallengedScoreHigher_returnsChallengedUser() {
        Challenge challenge = createChallenge();
        Round round = new Round(challenge, 1, RoundStatus.COMPLETED);
        round.setChallengerScore(60);
        round.setChallengedScore(80);

        User winner = round.winner();

        assertEquals(challenge.getChallenged(), winner);
    }

    @Test
    void winner_whenScoresEqual_returnsChallengedUser() {
        Challenge challenge = createChallenge();
        Round round = new Round(challenge, 1, RoundStatus.COMPLETED);
        round.setChallengerScore(75);
        round.setChallengedScore(75);

        User winner = round.winner();

        assertEquals(challenge.getChallenged(), winner);
    }

    @Test
    void answer_withWaitingChallengerState_processesAnswer() {
        Challenge challenge = createChallenge();
        Round round = new Round(challenge, 1, RoundStatus.WAITING_CHALLENGER);
        AnswerDTO answer = new AnswerDTO();
        answer.setRol(ChallengeRol.CHALLENGER);
        answer.setScore(90);
        answer.setRoundNumber(1);

        round.answer(answer);

        assertEquals(90, round.getChallengerScore());
        assertEquals(RoundStatus.WAITING_CHALLENGED, round.getStatus());
    }

    @Test
    void answer_withWaitingChallengedState_processesAnswer() {
        Challenge challenge = createChallenge();
        Round round = new Round(challenge, 1, RoundStatus.WAITING_CHALLENGED);
        AnswerDTO answer = new AnswerDTO();
        answer.setRol(ChallengeRol.CHALLENGED);
        answer.setScore(85);
        answer.setRoundNumber(1);

        round.answer(answer);

        assertEquals(85, round.getChallengedScore());
        assertEquals(RoundStatus.COMPLETED, round.getStatus());
    }

    @Test
    void answer_withCompletedState_throwsException() {
        Challenge challenge = createChallenge();
        Round round = new Round(challenge, 1, RoundStatus.COMPLETED);
        AnswerDTO answer = new AnswerDTO();
        answer.setRol(ChallengeRol.CHALLENGER);
        answer.setScore(95);

        assertThrows(RoundCompletedException.class, () -> {
            round.answer(answer);
        });
    }

    @Test
    void getState_withWaitingChallenger_returnsWaitingChallengerState() {
        Challenge challenge = createChallenge();
        Round round = new Round(challenge, 1, RoundStatus.WAITING_CHALLENGER);

        RoundState state = round.getState();

        assertInstanceOf(WaitingChallengerState.class, state);
    }

    @Test
    void getState_withWaitingChallenged_returnsWaitingChallengedState() {
        Challenge challenge = createChallenge();
        Round round = new Round(challenge, 1, RoundStatus.WAITING_CHALLENGED);

        RoundState state = round.getState();

        assertInstanceOf(WaitingChallengedState.class, state);
    }

    @Test
    void getState_withCompleted_returnsCompletedState() {
        Challenge challenge = createChallenge();
        Round round = new Round(challenge, 1, RoundStatus.COMPLETED);

        RoundState state = round.getState();

        assertInstanceOf(CompletedState.class, state);
    }

    @Test
    void isCompleted_withCompletedStatus_returnsTrue() {
        Challenge challenge = createChallenge();
        Round round = new Round(challenge, 1, RoundStatus.COMPLETED);

        assertTrue(round.isCompleted());
    }

    @Test
    void isCompleted_withWaitingChallengerStatus_returnsFalse() {
        Challenge challenge = createChallenge();
        Round round = new Round(challenge, 1, RoundStatus.WAITING_CHALLENGER);

        assertFalse(round.isCompleted());
    }

    @Test
    void isCompleted_withWaitingChallengedStatus_returnsFalse() {
        Challenge challenge = createChallenge();
        Round round = new Round(challenge, 1, RoundStatus.WAITING_CHALLENGED);

        assertFalse(round.isCompleted());
    }

    @Test
    void isWaitingChallenger_withWaitingChallengerStatus_returnsTrue() {
        Challenge challenge = createChallenge();
        Round round = new Round(challenge, 1, RoundStatus.WAITING_CHALLENGER);

        assertTrue(round.isWaitingChallenger());
    }

    @Test
    void isWaitingChallenger_withOtherStatuses_returnsFalse() {
        Challenge challenge = createChallenge();

        Round round1 = new Round(challenge, 1, RoundStatus.WAITING_CHALLENGED);
        assertFalse(round1.isWaitingChallenger());

        Round round2 = new Round(challenge, 1, RoundStatus.COMPLETED);
        assertFalse(round2.isWaitingChallenger());
    }

    @Test
    void isWaitingChallenged_withWaitingChallengedStatus_returnsTrue() {
        Challenge challenge = createChallenge();
        Round round = new Round(challenge, 1, RoundStatus.WAITING_CHALLENGED);

        assertTrue(round.isWaitingChallenged());
    }

    @Test
    void isWaitingChallenged_withOtherStatuses_returnsFalse() {
        Challenge challenge = createChallenge();

        Round round1 = new Round(challenge, 1, RoundStatus.WAITING_CHALLENGER);
        assertFalse(round1.isWaitingChallenged());

        Round round2 = new Round(challenge, 1, RoundStatus.COMPLETED);
        assertFalse(round2.isWaitingChallenged());
    }

    @Test
    void setters_workCorrectly() {
        Round round = new Round();
        Challenge challenge = createChallenge();

        round.setId(100L);
        round.setChallenge(challenge);
        round.setRoundNumber(5);
        round.setChallengerScore(88);
        round.setChallengedScore(92);
        round.setStatus(RoundStatus.COMPLETED);

        assertEquals(100L, round.getId());
        assertEquals(challenge, round.getChallenge());
        assertEquals(5, round.getRoundNumber());
        assertEquals(88, round.getChallengerScore());
        assertEquals(92, round.getChallengedScore());
        assertEquals(RoundStatus.COMPLETED, round.getStatus());
    }

    @Test
    void getters_workCorrectly() {
        Challenge challenge = createChallenge();
        Round round = new Round(challenge, 2, RoundStatus.WAITING_CHALLENGER);
        round.setId(50L);
        round.setChallengerScore(77);
        round.setChallengedScore(83);

        assertEquals(50L, round.getId());
        assertEquals(challenge, round.getChallenge());
        assertEquals(2, round.getRoundNumber());
        assertEquals(77, round.getChallengerScore());
        assertEquals(83, round.getChallengedScore());
        assertEquals(RoundStatus.WAITING_CHALLENGER, round.getStatus());
    }

    @Test
    void winner_withZeroScores_returnsChallengedUser() {
        Challenge challenge = createChallenge();
        Round round = new Round(challenge, 1, RoundStatus.COMPLETED);

        User winner = round.winner();

        assertEquals(challenge.getChallenged(), winner);
    }

    @Test
    void winner_withNegativeScores_worksCorrectly() {
        Challenge challenge = createChallenge();
        Round round = new Round(challenge, 1, RoundStatus.COMPLETED);
        round.setChallengerScore(-10);
        round.setChallengedScore(-20);

        User winner = round.winner();

        assertEquals(challenge.getChallenger(), winner);
    }

    private Challenge createChallenge() {
        User challenger = new User();
        challenger.setId(1L);
        challenger.setUsername("challenger");

        User challenged = new User();
        challenged.setId(2L);
        challenged.setUsername("challenged");

        Challenge challenge = new Challenge();
        challenge.setId(10L);
        challenge.setChallenger(challenger);
        challenge.setChallenged(challenged);
        challenge.setMaxRounds(3);
        challenge.setQuestionsPerRound(10);

        return challenge;
    }
}