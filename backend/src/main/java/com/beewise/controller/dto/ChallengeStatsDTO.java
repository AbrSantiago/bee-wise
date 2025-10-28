package com.beewise.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChallengeStatsDTO {
    private int userScore;
    private int opponentScore;
    private int userRoundsWon;
    private int opponentRoundsWon;
}
