package com.beewise.service;

import com.beewise.controller.dto.AnswerDTO;
import com.beewise.controller.dto.SendChallengeDTO;
import com.beewise.model.Challenge;

public interface ChallengeService {
    Challenge sendChallenge(SendChallengeDTO challengeDTO);
    Challenge acceptChallenge(Long challengeId);
    Challenge answerRound(AnswerDTO answer);
}
