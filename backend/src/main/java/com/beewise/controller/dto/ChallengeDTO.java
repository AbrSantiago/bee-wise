package com.beewise.controller.dto;

import com.beewise.model.Challenge;
import com.beewise.model.ChallengeResult;
import com.beewise.model.ChallengeStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ChallengeDTO {
    private Long id;
    private Long challengerId;
    private Long challengedId;
    private ChallengeStatus status;
    private List<RoundDTO> rounds;
    private LocalDate creationDate;
    private LocalDate expireDate;
    private ChallengeResult result;

    public ChallengeDTO(Challenge challenge) {
        this.id = challenge.getId();
        this.challengerId = challenge.getChallenger().getId();
        this.challengedId = challenge.getChallenged().getId();
        this.status = challenge.getStatus();
        this.rounds = challenge.getRounds()
                .stream()
                .map(RoundDTO::fromChallengeRound)
                .toList();
        this.creationDate = challenge.getCreationDate();
        this.expireDate = challenge.getExpireDate();
        this.result = challenge.getResult();
    }

    public static ChallengeDTO fromChallenge(Challenge challenge) {
        return new ChallengeDTO(challenge);
    }
}
