package com.beewise.service.impl;

import com.beewise.controller.dto.AnswerDTO;
import com.beewise.controller.dto.RewardDTO;
import com.beewise.controller.dto.SendChallengeDTO;
import com.beewise.controller.dto.UserDTO;
import com.beewise.exception.*;
import com.beewise.model.*;
import com.beewise.model.challenge.*;
import com.beewise.repository.ChallengeRepository;
import com.beewise.repository.RewardRepository;
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
    private final RewardRepository rewardRepository;
    private final ShopItemRepository shopItemRepository;
    private final UserRepository userRepository;

    public ChallengeServiceImpl(ChallengeRepository repository, UserService userService, ExerciseService exerciseService, RewardRepository rewardRepository, ShopItemRepository shopItemRepository, UserRepository userRepository) {
        this.repository = repository;
        this.userService = userService;
        this.exerciseService = exerciseService;
        this.rewardRepository = rewardRepository;
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
            if(!rewardRepository.existsByChallengeId(challenge.getId())){
                calculateAndPersistReward(challenge);
            }
        }
        return repository.save(challenge);
    }

    private void calculateAndPersistReward(Challenge challenge) {
        User challenger = challenge.getChallenger();
        User challenged = challenge.getChallenged();
        List<Round> rounds = challenge.getRounds();
        int totalRounds = rounds.size();

        int challengerPoints = 0;
        int challengedPoints = 0;

        for (Round round : rounds) {
            int challengerRoundPoints = round.getChallengerCorrectAnswers() * 2;
            int challengedRoundPoints = round.getChallengedCorrectAnswers() * 2;

            if (round.isChallengerPerfectRound()) {
                challengerRoundPoints *= 2;
            }
            if (round.isChallengedPerfectRound()) {
                challengedRoundPoints *= 2;
            }
            challengerPoints += challengerRoundPoints;
            challengedPoints += challengedRoundPoints;
        }

        int baseCoins = challenge.getQuestionsPerRound() * totalRounds;
        int challengerCoins = baseCoins;
        int challengedCoins = baseCoins;

        ShopItem challengerItem = null;
        ShopItem challengedItem = null;

        if (challenge.getResult() == ChallengeResult.CHALLENGER_WIN) {
            challengerPoints += (5 * totalRounds);
            challengerCoins *= 2;
            challengerItem = getRandomAvailableItem(challenger);

            if (challengerItem == null) {
                challengerPoints += 10;
            }
        } else if (challenge.getResult() == ChallengeResult.CHALLENGED_WIN) {
            challengedPoints += (5 * totalRounds);
            challengedCoins *= 2;
            challengedItem = getRandomAvailableItem(challenged);

            if (challengedItem == null) {
                challengedPoints += 10;
            }
        }

        challenger.setPoints(challenger.getPoints() + challengerPoints);
        challenger.setBeeCoins(challenger.getBeeCoins() + challengerCoins);
        if (challengerItem != null) {
            challenger.getItems().add(challengerItem);
        }

        challenged.setPoints(challenged.getPoints() + challengedPoints);
        challenged.setBeeCoins(challenged.getBeeCoins() + challengedCoins);
        if (challengedItem != null) {
            challenged.getItems().add(challengedItem);
        }

        Reward challengerReward = new Reward(challenger, challenge, challengerPoints, challengerCoins, challengerItem);
        Reward challengedReward = new Reward(challenged, challenge, challengedPoints, challengedCoins, challengedItem);

        rewardRepository.save(challengerReward);
        rewardRepository.save(challengedReward);

        userRepository.save(challenger);
        userRepository.save(challenged);
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
    public RewardDTO getRewards(Long challengeId, String username) {
        User user = userService.getUserByUsername(username);

        Reward reward = rewardRepository.findByChallengeIdAndUserId(challengeId,user.getId())
                .orElseThrow(() -> new RewardNotFoundException("Rewards not found for this challenge"));

        return new RewardDTO(reward);
    }


}
