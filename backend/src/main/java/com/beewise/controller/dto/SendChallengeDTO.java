package com.beewise.controller.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SendChallengeDTO {
    private Long challengerId;

    private Long challengedId;

    @Min(value = 1, message = "Rounds amount must be positive")
    private int maxRounds;

    @Min(value = 5, message = "Questions amount must be at least 5")
    private int questionsPerRound;
}
