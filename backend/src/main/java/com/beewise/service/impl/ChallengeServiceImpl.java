package com.beewise.service.impl;

import com.beewise.exception.ChallengeAlreadyExistsException;
import com.beewise.model.Challenge;
import com.beewise.model.ChallengeStatus;
import com.beewise.model.User;
import com.beewise.repository.ChallengeRepository;
import com.beewise.service.ChallengeService;
import com.beewise.service.UserService;

import java.util.List;

public class ChallengeServiceImpl implements ChallengeService {
    private final ChallengeRepository repository;
    private final UserService userService;

    public ChallengeServiceImpl(ChallengeRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @Override
    public Challenge newChallenge(Long challengerId, Long challengedId) {
        User challenger = userService.getUserById(challengerId);
        User challenged = userService.getUserById(challengedId);
        List<ChallengeStatus> statuses = List.of(ChallengeStatus.PENDING, ChallengeStatus.ACTIVE);
        if (repository.existsByChallengerAndChallengedAndStatusIn(challenger, challenged, statuses)) {
            throw new ChallengeAlreadyExistsException("Challenge already exists");
        };

        return null;
    }
}
