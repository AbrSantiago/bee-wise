package com.beewise.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AnswerDTO {
    private Long challengeId;
    private int roundNumber;
    private ChallengeRol rol;
    private int score;
}
