package com.beewise.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class ChallengeRound {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Long challengeId;

    private int roundNumber;

    private int challengerScore = 0;

    private int challengedScore = 0;

    @Enumerated(EnumType.STRING)
    private RoundStatus status;

    public ChallengeRound(Long challengeId, int roundNumber) {
        this.challengeId = challengeId;
        this.roundNumber = roundNumber;
        this.status = RoundStatus.WAITING_CHALLENGED;
    }
}
