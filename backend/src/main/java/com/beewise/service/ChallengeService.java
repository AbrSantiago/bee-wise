package com.beewise.service;

import com.beewise.controller.dto.AnswerDTO;
import com.beewise.controller.dto.RoundDTO;
import com.beewise.model.Challenge;

public interface ChallengeService {
    Challenge sendChallenge(Long challengerId, Long challengedId, int maxRounds, int questionsPerRound);
    Challenge acceptChallenge(Long challengeId);
    Challenge answerRound(AnswerDTO answer);
}
