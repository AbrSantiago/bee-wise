package com.beewise.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserStatsDTO {
    private int challengesPlayed;
    private int challengesWon;
    private int winRate;
    private int roundsWon;
    private int avgCorrectAnswersPerChallenge;

    private int totalPoints;
    private int beeCoinsSpent;
    private int longestWinStreak;
    private int itemsObtained;

    private int percentile;
    private int accuracy;
}
