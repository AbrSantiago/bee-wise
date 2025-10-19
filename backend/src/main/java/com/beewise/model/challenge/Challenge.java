package com.beewise.model.challenge;

import com.beewise.controller.dto.ChallengeRol;
import com.beewise.exception.ChallengeAlreadyCompletedException;
import com.beewise.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenger_id", nullable = false)
    private User challenger;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenged_id", nullable = false)
    private User challenged;

    @Enumerated(EnumType.STRING)
    private ChallengeStatus status = ChallengeStatus.PENDING;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("roundNumber ASC")
    private List<Round> rounds = new ArrayList<>();

    @Min(value = 1, message = "Rounds amount must be positive")
    private int maxRounds = 3;

    @Min(value = 5, message = "Questions amount must be at least 5")
    private int questionsPerRound = 10;

    private LocalDate creationDate;

    private LocalDate expireDate;

    @Enumerated(EnumType.STRING)
    private ChallengeResult result = null;

    public Challenge(User challenger, User challenged, int maxRounds, int questionsPerRound) {
        this.challenger = challenger;
        this.challenged = challenged;
        this.maxRounds = maxRounds;
        this.questionsPerRound = questionsPerRound;
        this.creationDate = LocalDate.now();
        this.expireDate = LocalDate.now().plusDays(3);
    }

    public User getNextUserToPlay() {
        Round round = rounds.stream().max(Comparator.comparing(Round::getRoundNumber))
                .orElseThrow();
        return switch (round.getStatus()) {
            case WAITING_CHALLENGED -> challenger;
            case WAITING_CHALLENGER -> challenged;
            case COMPLETED -> throw new ChallengeAlreadyCompletedException("Challenge " + id + " was already completed");
        };
    }
}
