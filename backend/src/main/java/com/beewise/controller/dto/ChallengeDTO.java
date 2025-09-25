package com.beewise.controller.dto;

import com.beewise.model.challenge.Challenge;
import com.beewise.model.challenge.ChallengeResult;
import com.beewise.model.challenge.ChallengeStatus;
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
    private int maxRounds;
    private int questionsPerRound;
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
        this.maxRounds = challenge.getMaxRounds();
        this.questionsPerRound = challenge.getQuestionsPerRound();
        this.creationDate = challenge.getCreationDate();
        this.expireDate = challenge.getExpireDate();
        this.result = challenge.getResult();
    }

    public static ChallengeDTO fromChallenge(Challenge challenge) {
        return new ChallengeDTO(challenge);
    }
}
