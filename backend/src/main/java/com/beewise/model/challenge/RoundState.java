package com.beewise.model.challenge;

import com.beewise.controller.dto.AnswerDTO;

public interface RoundState {
    void answer(Challenge challenge, Round round, AnswerDTO answer);
}
