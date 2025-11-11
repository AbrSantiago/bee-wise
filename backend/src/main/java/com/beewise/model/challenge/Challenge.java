package com.beewise.model.challenge;

import com.beewise.controller.dto.*;
import com.beewise.exception.ChallengeAlreadyCompletedException;
import com.beewise.exception.ChallengeNotCompleteYetException;
import com.beewise.exception.UserNotPlayingChallengeException;
import com.beewise.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenger_id", nullable = false)
    private User challenger;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenged_id", nullable = false)
    private User challenged;

    @Enumerated(EnumType.STRING)
    private ChallengeStatus status = ChallengeStatus.PENDING;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("roundNumber ASC")
    private List<Round> rounds = new ArrayList<>();

    @Min(value = 1, message = "Rounds amount must be positive")
    private int maxRounds = 3;

    @Min(value = 5, message = "Questions amount must be at least 5")
    private int questionsPerRound = 10;

    private LocalDate creationDate;

    private LocalDate expireDate;

    @Enumerated(EnumType.STRING)
    private ChallengeResult result = null;

    private boolean challengerGotReward = false;

    private boolean challengedGotReward = false;

    public Challenge(User challenger, User challenged, int maxRounds, int questionsPerRound) {
        this.challenger = challenger;
        this.challenged = challenged;
        this.maxRounds = maxRounds;
        this.questionsPerRound = questionsPerRound;
        this.creationDate = LocalDate.now();
        this.expireDate = LocalDate.now().plusDays(3);
    }

    public User getNextUserToPlay() {
        Round round = rounds.stream().max(Comparator.comparing(Round::getRoundNumber))
                .orElseThrow();
        return switch (round.getStatus()) {
            case WAITING_CHALLENGED -> challenger;
            case WAITING_CHALLENGER -> challenged;
            case COMPLETED -> throw new ChallengeAlreadyCompletedException("Challenge " + id + " was already completed");
        };
    }

    public ChallengeStatsDTO getStats(String username) {
        User user = getNextUserToPlay();
        if (!Objects.equals(user.getUsername(), username)) {
            throw new UserNotPlayingChallengeException("User " + username + " is not playing challenge " + id);
        }
        Round currentRound = rounds.stream().max(Comparator.comparing(Round::getRoundNumber))
                .orElseThrow();
        ChallengeStatsDTO statsDTO = new ChallengeStatsDTO();
        if (user == challenger) {
            statsDTO.setUserScore(currentRound.getChallengerScore());
            statsDTO.setOpponentScore(currentRound.getChallengedScore());
            statsDTO.setUserRoundsWon(getUserRoundsWon(challenger));
            statsDTO.setOpponentRoundsWon(getUserRoundsWon(challenger));
        } else {
            statsDTO.setUserScore(currentRound.getChallengedScore());
            statsDTO.setOpponentScore(currentRound.getChallengerScore());
            statsDTO.setUserRoundsWon(getUserRoundsWon(challenged));
            statsDTO.setOpponentRoundsWon(getUserRoundsWon(challenged));
        }
        return statsDTO;
    }

    public ChallengeSummaryDTO getSummary(String username) {
        if (result == null) {
            throw new ChallengeNotCompleteYetException("Challenge is not completed yet");
        }

        ChallengeSummaryDTO summaryDTO = new ChallengeSummaryDTO();
        summaryDTO.setUsername(username);
        summaryDTO.setTotalRounds(maxRounds);
        summaryDTO.setBeeCoins(getRewardBeeCoins(username));
        summaryDTO.setPoints(getRewardPoints(username));
        if (Objects.equals(username, challenger.getUsername())) {
            summaryDTO.setAvatar(new AvatarDTO(challenger.getAvatar()));
            summaryDTO.setOpponentUsername(challenged.getUsername());
            summaryDTO.setOpponentAvatar(new AvatarDTO(challenged.getAvatar()));
            summaryDTO.setRoundsWon(getUserRoundsWon(challenger));
            switch (result) {
                case CHALLENGER_WIN -> summaryDTO.setWinner(ChallengeWinner.ME);
                case CHALLENGED_WIN -> summaryDTO.setWinner(ChallengeWinner.OPPONENT);
                case DRAW -> summaryDTO.setWinner(ChallengeWinner.DRAW);
            }
        } else if (Objects.equals(username, challenged.getUsername())) {
            summaryDTO.setAvatar(new AvatarDTO(challenged.getAvatar()));
            summaryDTO.setOpponentUsername(challenger.getUsername());
            summaryDTO.setOpponentAvatar(new AvatarDTO(challenger.getAvatar()));
            summaryDTO.setRoundsWon(getUserRoundsWon(challenged));
            switch (result) {
                case CHALLENGER_WIN -> summaryDTO.setWinner(ChallengeWinner.OPPONENT);
                case CHALLENGED_WIN -> summaryDTO.setWinner(ChallengeWinner.ME);
                case DRAW -> summaryDTO.setWinner(ChallengeWinner.DRAW);
            }
        }
        return summaryDTO;
    }

    public int getRewardBeeCoins(String username) {
        int beeCoins = questionsPerRound * maxRounds;
        if (Objects.equals(username, challenger.getUsername())) {
            if (result == ChallengeResult.CHALLENGER_WIN) {
                beeCoins += 5 * maxRounds;
            }
        } else if (Objects.equals(username, challenged.getUsername())) {
            if (result == ChallengeResult.CHALLENGED_WIN) {
                beeCoins += 5 * maxRounds;
            }
        } else {
            throw new UserNotPlayingChallengeException("User " + username + " is not playing this challenge");
        }
        return beeCoins;
    }

    public int getRewardPoints(String username) {
        int points = 0;
        if (Objects.equals(username, challenger.getUsername())) {
            for (Round round : rounds) {
                points += round.getChallengerScore() * 2;
            }
            if (result == ChallengeResult.CHALLENGER_WIN) {
                points += 5 * maxRounds;
            }
        } else if (Objects.equals(username, challenged.getUsername())) {
            for (Round round : rounds) {
                points += round.getChallengedScore() * 2;
            }
            if (result == ChallengeResult.CHALLENGED_WIN) {
                points += 5 * maxRounds;
            }
        } else {
            throw new UserNotPlayingChallengeException("User " + username + " is not playing this challenge");
        }
        return points;
    }

    private int getUserRoundsWon(User user) {
        return (int) rounds.stream().filter(r -> r.winner() == user).count();
    }

    public int getChallengerRewardPoints() {
        int points = 0;
        for (Round round : rounds) {
            points += round.getChallengerScore() * 2;
        }
        if (result == ChallengeResult.CHALLENGER_WIN) {
            points += 5 * maxRounds;
        }
        return points;
    }

    public int getChallengedRewardPoints() {
        int points = 0;
        for (Round round : rounds) {
            points += round.getChallengedScore() * 2;
        }
        if (result == ChallengeResult.CHALLENGED_WIN) {
            points += 5 * maxRounds;
        }
        return points;
    }

    public int getChallengerRewardBeeCoins() {
        int beeCoins = questionsPerRound * maxRounds;
        if (result == ChallengeResult.CHALLENGER_WIN) {
            beeCoins += 5 * maxRounds;
        }
        return beeCoins;
    }

    public int getChallengedRewardBeeCoins() {
        int beeCoins = questionsPerRound * maxRounds;
        if (result == ChallengeResult.CHALLENGED_WIN) {
            beeCoins += 5 * maxRounds;
        }
        return beeCoins;
    }

    public ChallengeRol getRol(String username) {
        if (isChallenger(username)) {
            return ChallengeRol.CHALLENGER;
        } else if (isChallenged(username)) {
            return ChallengeRol.CHALLENGED;
        } else {
            throw new UserNotPlayingChallengeException("User " + username + " is not playing this challenge");
        }
    }

    private boolean isChallenger(String username) {
        return Objects.equals(challenger.getUsername(), username);
    }

    private boolean isChallenged(String username) {
        return Objects.equals(challenged.getUsername(), username);
    }
}
