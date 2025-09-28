package com.beewise.service;

import com.beewise.controller.dto.AnswerDTO;
import com.beewise.controller.dto.SendChallengeDTO;
import com.beewise.model.User;
import com.beewise.model.challenge.Challenge;

import java.util.List;

public interface ChallengeService {
    Challenge sendChallenge(SendChallengeDTO challengeDTO);
    Challenge acceptChallenge(Long challengeId);
    Challenge answerRound(AnswerDTO answer);
    List<Challenge> getAll();
    List<User> getUsersToChallenge(Long challengerId);
}
