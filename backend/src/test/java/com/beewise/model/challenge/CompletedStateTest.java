package com.beewise.model.challenge;

import com.beewise.controller.dto.AnswerDTO;
import com.beewise.controller.dto.ChallengeRol;
import com.beewise.exception.RoundCompletedException;
import com.beewise.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompletedStateTest {

    @Test
    void answer_throwsRoundCompletedException() {
        CompletedState state = new CompletedState();
        Challenge challenge = createChallenge();
        Round round = new Round(challenge, 1, RoundStatus.COMPLETED);
        AnswerDTO answer = new AnswerDTO();
        answer.setRol(ChallengeRol.CHALLENGER);
        answer.setScore(85);

        RoundCompletedException exception = assertThrows(RoundCompletedException.class, () -> {
            state.answer(challenge, round, answer);
        });

        assertEquals("Round already completed", exception.getMessage());
    }

    @Test
    void answer_withDifferentRol_stillThrowsException() {
        CompletedState state = new CompletedState();
        Challenge challenge = createChallenge();
        Round round = new Round(challenge, 2, RoundStatus.COMPLETED);
        AnswerDTO answer = new AnswerDTO();
        answer.setRol(ChallengeRol.CHALLENGED);
        answer.setScore(90);

        assertThrows(RoundCompletedException.class, () -> {
            state.answer(challenge, round, answer);
        });
    }

    private Challenge createChallenge() {
        User challenger = new User();
        challenger.setId(1L);
        User challenged = new User();
        challenged.setId(2L);
        return new Challenge(challenger, challenged, 3, 10);
    }
}