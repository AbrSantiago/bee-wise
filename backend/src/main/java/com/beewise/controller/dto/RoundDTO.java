package com.beewise.controller.dto;

import com.beewise.model.challenge.Round;
import com.beewise.model.challenge.RoundStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoundDTO {
    private Long id;
    private Long challengeId;
    private int roundNumber;
    private int challengerScore;
    private int challengedScore;
    private RoundStatus status;

    public RoundDTO(Round round) {
        this.id = round.getId();
        this.challengeId = round.getChallenge().getId();
        this.roundNumber = round.getRoundNumber();
        this.challengerScore = round.getChallengerScore();
        this.challengedScore = round.getChallengedScore();
        this.status = round.getStatus();
    }

    public static RoundDTO fromChallengeRound(Round round) {
        return new RoundDTO(round);
    }
}
