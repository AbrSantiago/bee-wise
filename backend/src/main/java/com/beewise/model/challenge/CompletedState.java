package com.beewise.model.challenge;

import com.beewise.controller.dto.AnswerDTO;
import com.beewise.exception.RoundCompletedException;

public class CompletedState implements RoundState {
    @Override
    public void answer(Challenge challenge, Round round, AnswerDTO answer) {
        throw new RoundCompletedException("Round already completed");
    }
}
