package com.beewise.controller.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AnswerDTO {
    private Long challengeId;

    @Min(value = 1, message = "Round cannot be negative")
    private int roundNumber;

    @Enumerated(EnumType.STRING)
    private ChallengeRol rol;

    @Min(value = 0, message = "Score cannot be negative")
    private int score;
}
