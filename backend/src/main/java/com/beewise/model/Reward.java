package com.beewise.model;

import com.beewise.model.challenge.Challenge;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    @Column(nullable = false)
    private int pointsGained;

    @Column(nullable = false)
    private int beeCoinsGained;

    @ManyToOne
    @JoinColumn(name = "item_gained_id", nullable = true)
    private ShopItem itemGained;

    public Reward(User user, Challenge challenge, int pointsGained, int beeCoinsGained, ShopItem itemGained) {
        this.user = user;
        this.challenge = challenge;
        this.pointsGained = pointsGained;
        this.beeCoinsGained = beeCoinsGained;
        this.itemGained = itemGained;
    }
}
