package com.beewise.model.challenge;

import com.beewise.controller.dto.AnswerDTO;
import com.beewise.controller.dto.ChallengeRol;
import com.beewise.exception.AnswerWrongRolException;

public class WaitingChallengedState implements RoundState {
    @Override
    public void answer(Challenge challenge, Round round, AnswerDTO answer) {
        if (answer.getRol() != ChallengeRol.CHALLENGED) {
            throw new AnswerWrongRolException("Rol should be CHALLENGED");
        }
        round.setChallengedScore(answer.getScore());
        if (isEndedRound(answer)) {
            round.setStatus(RoundStatus.COMPLETED);
            createNextRoundIfNeeded(challenge, answer);
        } else {
            round.setStatus(RoundStatus.WAITING_CHALLENGER);
        }
    }

    private static void createNextRoundIfNeeded(Challenge challenge, AnswerDTO answer) {
        if (mustCreateNextRound(challenge, answer)) {
            Round nextRound = new Round(challenge, answer.getRoundNumber() + 1, RoundStatus.WAITING_CHALLENGED);
            challenge.getRounds().add(nextRound);
        }
    }

    private static boolean mustCreateNextRound(Challenge challenge, AnswerDTO answer) {
        return answer.getRoundNumber() + 1 <= challenge.getMaxRounds();
    }

    private static boolean isEndedRound(AnswerDTO answer) {
        return answer.getRoundNumber() % 2 == 1;
    }
}
