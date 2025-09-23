package com.beewise.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(
    uniqueConstraints = @UniqueConstraint(
            columnNames = {"challenger_id", "challenged_id", "status"}
    )
)
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
}
