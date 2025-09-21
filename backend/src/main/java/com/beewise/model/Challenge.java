package com.beewise.model;

import jakarta.persistence.*;
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
    private ChallengeStatus status;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChallengeRound> rounds = new ArrayList<>();

    private LocalDate creationDate;

    private LocalDate expireDate;

    @Enumerated(EnumType.STRING)
    private ChallengeResult result = null;

    public Challenge(User challenger, User challenged) {
        this.challenger = challenger;
        this.challenged = challenged;
        this.status = ChallengeStatus.PENDING;
        this.creationDate = LocalDate.now();
        this.expireDate = LocalDate.now().plusDays(3);
    }
}
