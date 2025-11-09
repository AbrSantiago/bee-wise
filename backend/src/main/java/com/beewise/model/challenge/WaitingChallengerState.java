package com.beewise.model.challenge;

import com.beewise.controller.dto.AnswerDTO;
import com.beewise.controller.dto.ChallengeRol;
import com.beewise.exception.AnswerWrongRolException;

public class WaitingChallengerState implements RoundState {
    @Override
    public void answer(Challenge challenge, Round round, AnswerDTO answer) {
        if (answer.getRol() != ChallengeRol.CHALLENGER) {
            throw new AnswerWrongRolException("Rol should be CHALLENGER");
        }
        round.setChallengerScore(answer.getScore());
        if (isEndedRound(answer)) {
            round.setStatus(RoundStatus.COMPLETED);
            createNextRoundIfNeeded(challenge, answer);
        } else {
            round.setStatus(RoundStatus.WAITING_CHALLENGED);
        }
    }

    private static void createNextRoundIfNeeded(Challenge challenge, AnswerDTO answer) {
        if (mustCreateNextRound(challenge, answer)) {
            Round nextRound = new Round(challenge, answer.getRoundNumber() + 1, RoundStatus.WAITING_CHALLENGER);
            challenge.getRounds().add(nextRound);
        }
    }

    private static boolean mustCreateNextRound(Challenge challenge, AnswerDTO answer) {
        return answer.getRoundNumber() + 1 <= challenge.getMaxRounds();
    }

    private static boolean isEndedRound(AnswerDTO answer) {
        return answer.getRoundNumber() % 2 == 0;
    }
}
