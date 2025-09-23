package com.beewise.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Round {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    private int roundNumber;

    private int challengerScore = 0;

    private int challengedScore = 0;

    @Enumerated(EnumType.STRING)
    private RoundStatus status;

    public Round(Challenge challenge, int roundNumber, RoundStatus status) {
        this.challenge = challenge;
        this.roundNumber = roundNumber;
        this.status = status;
    }
}
