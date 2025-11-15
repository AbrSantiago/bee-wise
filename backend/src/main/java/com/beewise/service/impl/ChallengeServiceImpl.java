package com.beewise.service.impl;

import com.beewise.controller.dto.*;
import com.beewise.exception.*;
import com.beewise.model.*;
import com.beewise.model.challenge.*;
import com.beewise.repository.ChallengeRepository;
import com.beewise.repository.ShopItemRepository;
import com.beewise.repository.UserRepository;
import com.beewise.service.ChallengeService;
import com.beewise.service.ExerciseService;
import com.beewise.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
@Transactional
public class ChallengeServiceImpl implements ChallengeService {
    private final ChallengeRepository repository;
    private final UserService userService;
    private final ExerciseService exerciseService;
    private final ShopItemRepository shopItemRepository;
    private final UserRepository userRepository;

    public ChallengeServiceImpl(
            ChallengeRepository repository,
            UserService userService,
            ExerciseService exerciseService,
            ShopItemRepository shopItemRepository,
            UserRepository userRepository
    ) {
        this.repository = repository;
        this.userService = userService;
        this.exerciseService = exerciseService;
        this.shopItemRepository = shopItemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Challenge sendChallenge(SendChallengeDTO dto) {
        if (Objects.equals(dto.getChallengerId(), dto.getChallengedId())) {
            throw new UserChallengesHimselfException("User " + dto.getChallengerId() + " cannot challenge himself");
        }
        User challenger = userService.getUserById(dto.getChallengerId());
        User challenged = userService.getUserById(dto.getChallengedId());
        List<ChallengeStatus> statuses = List.of(ChallengeStatus.PENDING, ChallengeStatus.ACTIVE);
        if (repository.existsByChallengerAndChallengedAndStatusIn(challenger, challenged, statuses) ||
                repository.existsByChallengerAndChallengedAndStatusIn(challenged, challenger, statuses)) {
            throw new ChallengeAlreadyExistsException("Challenge between users " + dto.getChallengerId()
                    + " and " + dto.getChallengedId() + " already exists");
        }
        Challenge challenge = new Challenge(challenger, challenged, dto.getMaxRounds(), dto.getQuestionsPerRound());
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
        if (challenge.getRounds().get(0).isWaitingChallenger()) {
            throw new WaitingFotChallengerException("Challenger has not played yet");
        }
        challenge.setStatus(ChallengeStatus.ACTIVE);
        return repository.save(challenge);
    }

    @Override
    public Challenge answerRound(AnswerDTO answer) {
        Challenge challenge = repository.findById(answer.getChallengeId())
                .orElseThrow(() -> new ChallengeNotFoundException("Challenge with id " + answer.getChallengeId() + " does not exists"));
        List<Round> rounds = challenge.getRounds();
        if (challenge.getStatus() != ChallengeStatus.ACTIVE && rounds.get(0).isWaitingChallenged()){
            throw new AnswerNotAllowedException("Not allowed to answer");
        }
        if (answer.getRoundNumber() > challenge.getMaxRounds() || answer.getRoundNumber() != rounds.size()) {
            throw new RoundNumberException("Wrong round number");
        }

        Round round = rounds.get(answer.getRoundNumber() - 1);
        round.answer(answer);

        // CHECK IF ENDS
        if (answer.getRoundNumber() == challenge.getMaxRounds() && round.isCompleted()) {
            challenge.setStatus(ChallengeStatus.COMPLETED);
            long challengerWins = rounds.stream().filter(r -> r.winner() == challenge.getChallenger()).count();
            long challengedWins = rounds.stream().filter(r -> r.winner() == challenge.getChallenged()).count();
            if (challengerWins == challengedWins) {
                challenge.setResult(ChallengeResult.DRAW);
            } else if (challengerWins > challengedWins) {
                challenge.setResult(ChallengeResult.CHALLENGER_WIN);
            } else {
                challenge.setResult(ChallengeResult.CHALLENGED_WIN);
            }
        }
        return repository.save(challenge);
    }

    private ShopItem getRandomAvailableItem(User user) {
        List<ShopItem> allItems = shopItemRepository.findAll();

        List<ShopItem> availableItems = allItems.stream()
                .filter(item -> !user.getItems().contains(item))
                .toList();

        if (availableItems.isEmpty()) {
            return null;
        }

        Random random = new Random();
        int randomIndex = random.nextInt(availableItems.size());
        return availableItems.get(randomIndex);
    }

    @Override
    public List<Challenge> getAll() {
        return repository.findAll();
    }

    @Override
    public List<User> getUsersToChallenge(Long challengerId) {
        return userService.getUsersToChallenge(
                challengerId,
                List.of(ChallengeStatus.PENDING, ChallengeStatus.ACTIVE)
        );
    }

    @Override
    public List<Exercise> getRandomExercises(int limit, ExerciseCategory category) {
        return exerciseService.getRandomExercises(limit, category);
    }

    @Override
    public ExerciseCategory getRandomCategory() {
        ExerciseCategory[] categories = ExerciseCategory.values();
        int randomIndex = new Random().nextInt(categories.length);
        return categories[randomIndex];
    }

    @Override
    public User getOpponent(Long challengeId, String username) {
        Challenge challenge = repository.findById(challengeId)
                .orElseThrow(() -> new ChallengeNotFoundException("Challenge with id " + challengeId + " does not exists"));
        return challenge.getNextUserToPlay();
    }

    @Override
    public ChallengeStatsDTO getChallengeStats(Long challengeId, String username) {
        Challenge challenge = repository.findById(challengeId)
                .orElseThrow(() -> new ChallengeNotFoundException("Challenge with id " + challengeId + " does not exists"));
        return challenge.getStats(username);
    }

    @Override
    public User getChallengeOpponent(Long challengeId, String username) {
        Challenge challenge = repository.findById(challengeId)
                .orElseThrow(() -> new ChallengeNotFoundException("Challenge with id " + challengeId + " does not exists"));
        if (Objects.equals(challenge.getChallenger().getUsername(), username)) {
            return challenge.getChallenger();
        } else if (Objects.equals(challenge.getChallenged().getUsername(), username)) {
            return challenge.getChallenger();
        } else {
            throw new UserNotPlayingChallengeException("User " + username + " is no playing challenge " + challenge.getId());
        }
    }

    @Override
    public ChallengeSummaryDTO getSummaryAndReward(Long challengeId, String username) {
        Challenge challenge = repository.findById(challengeId)
                .orElseThrow(() -> new ChallengeNotFoundException("Challenge with id " + challengeId + " does not exists"));
        ChallengeSummaryDTO summaryDTO = challenge.getSummary(username);

        LevelUpInfoDTO levelUpInfo = null;

        switch (challenge.getRol(username)) {
            case CHALLENGER -> {
                if (challenge.isChallengerGotReward()) {
                    throw new UserAlreadyGotRewardException("User " + username + " already got reward");
                }
                User challenger = challenge.getChallenger();
                Level oldLevel = challenger.getLevel();

                if (challenge.challengerWon()) {
                    ShopItem randomItem = getRandomAvailableItem(challenger);
                    if (randomItem != null) {
                        summaryDTO.setItem(new ShopItemDTO(randomItem));
                        challenger.addItem(randomItem);
                    }
                }
                userService.addPointsToUser(challenger, challenge.getChallengerRewardPoints());

                if (!oldLevel.getId().equals(challenger.getLevel().getId())) {
                    levelUpInfo = new LevelUpInfoDTO(
                            oldLevel.getId(),
                            challenger.getLevel().getId(),
                            challenger.getLevel().getName(),
                            challenger.getLevel().getIconUrl()
                    );
                }

                challenger.addBeeCoins(challenge.getChallengerRewardBeeCoins());
                userRepository.save(challenger);
                challenge.setChallengerGotReward(true);
            }
            case CHALLENGED -> {
                if (challenge.isChallengedGotReward()) {
                    throw new UserAlreadyGotRewardException("User " + username + " already got reward");
                }
                User challenged = challenge.getChallenged();
                Level oldLevel = challenged.getLevel();

                if (challenge.challengedWon()) {
                    ShopItem randomItem = getRandomAvailableItem(challenged);
                    if (randomItem != null) {
                        summaryDTO.setItem(new ShopItemDTO(randomItem));
                        challenged.addItem(randomItem);
                    }
                }
                userService.addPointsToUser(challenged, challenge.getChallengedRewardPoints());
                if (!oldLevel.getId().equals(challenged.getLevel().getId())) {
                    levelUpInfo = new LevelUpInfoDTO(
                            oldLevel.getId(),
                            challenged.getLevel().getId(),
                            challenged.getLevel().getName(),
                            challenged.getLevel().getIconUrl()
                    );
                }

                challenged.addBeeCoins(challenge.getChallengedRewardBeeCoins());
                challenge.setChallengedGotReward(true);
                userRepository.save(challenged);
            }
        }
        summaryDTO.setLevelUp(levelUpInfo);
        repository.save(challenge);
        return summaryDTO;
    }
}
