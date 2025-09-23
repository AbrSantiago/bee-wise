package com.beewise.service.impl;

import com.beewise.controller.dto.AnswerDTO;
import com.beewise.controller.dto.ChallengeRol;
import com.beewise.exception.*;
import com.beewise.model.*;
import com.beewise.repository.ChallengeRepository;
import com.beewise.service.ChallengeService;
import com.beewise.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ChallengeServiceImpl implements ChallengeService {
    private final ChallengeRepository repository;
    private final UserService userService;

    public ChallengeServiceImpl(ChallengeRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @Override
    public Challenge sendChallenge(Long challengerId, Long challengedId, int maxRounds, int questionsPerRound) {
        User challenger = userService.getUserById(challengerId);
        User challenged = userService.getUserById(challengedId);
        List<ChallengeStatus> statuses = List.of(ChallengeStatus.PENDING, ChallengeStatus.ACTIVE);
        if (repository.existsByChallengerAndChallengedAndStatusIn(challenger, challenged, statuses)) {
            throw new ChallengeAlreadyExistsException("Challenge already exists");
        };
        Challenge challenge = new Challenge(challenger, challenged, maxRounds, questionsPerRound);
        challenge.getRounds().add(new Round(challenge, 1, RoundStatus.WAITING_CHALLENGER));
        return repository.save(challenge);
    }

    @Override
    public Challenge acceptChallenge(Long challengeId) {
        Challenge challenge = repository.findById(challengeId)
                .orElseThrow(() -> new ChallengeNotFoundException("Challenge with id " + challengeId + " does not exists"));
        if (!challenge.getStatus().equals(ChallengeStatus.PENDING)) {
            throw new ChallengeNotPendingException("Challenge with id " + challengeId + " was already accepted");
        }
        challenge.setStatus(ChallengeStatus.ACTIVE);
        return repository.save(challenge);
    }

    @Override
    public Challenge answerRound(AnswerDTO answer) {
        Challenge challenge = repository.findById(answer.getChallengeId())
                .orElseThrow(() -> new ChallengeNotFoundException("Challenge with id " + answer.getChallengeId() + " does not exists"));
        List<Round> rounds = challenge.getRounds();
        if (answer.getRoundNumber() > challenge.getMaxRounds() || answer.getRoundNumber() < rounds.size()) {
            throw new RoundNumberException("Wrong round number");
        }
        Round round = rounds.get(answer.getRoundNumber() - 1);
        if (round.getStatus().equals(RoundStatus.WAITING_CHALLENGED)) {
            if (!answer.getRol().equals(ChallengeRol.CHALLENGED)) {
                throw new AnswerWrongRolException("Rol should be CHALLENGED");
            } else {
                round.setChallengedScore(answer.getScore());
            }
        }
        if (round.getStatus().equals(RoundStatus.WAITING_CHALLENGER)) {
            if (!answer.getRol().equals(ChallengeRol.CHALLENGER)) {
                throw new AnswerWrongRolException("Rol should be CHALLENGER");
            } else {
                round.setChallengerScore(answer.getScore());
            }
        }
        if (round.getStatus().equals(RoundStatus.COMPLETED)) {
            throw new RoundCompletedException("Round was already completed");
        }
        return repository.save(challenge);
    }
}
