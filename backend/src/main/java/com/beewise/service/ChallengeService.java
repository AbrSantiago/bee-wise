package com.beewise.service;

import com.beewise.controller.dto.AnswerDTO;
import com.beewise.controller.dto.SendChallengeDTO;
import com.beewise.model.Challenge;

import java.util.List;

public interface ChallengeService {
    Challenge sendChallenge(SendChallengeDTO challengeDTO);
    Challenge acceptChallenge(Long challengeId);
    Challenge answerRound(AnswerDTO answer);
    List<Challenge> getAll();
}
