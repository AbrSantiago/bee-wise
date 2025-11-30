package com.beewise.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChallengeSummaryDTO {
    private String username;
    private AvatarDTO avatar;
    private String opponentUsername;
    private AvatarDTO opponentAvatar;
    private int roundsWon;
    private int totalRounds;
    private int beeCoins;
    private int points;
    private ShopItemDTO item = null;
    private ChallengeWinner winner;
    private LevelUpInfoDTO levelUp = null;
}
