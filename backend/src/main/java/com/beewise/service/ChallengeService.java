package com.beewise.service;

import com.beewise.model.Challenge;

public interface ChallengeService {
    Challenge newChallenge(Long challengerId, Long challengedId);
}
