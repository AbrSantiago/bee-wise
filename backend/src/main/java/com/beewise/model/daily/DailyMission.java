package com.beewise.model.daily;

import com.beewise.model.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class DailyMission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private MissionType type;

    private int goalAmount;

    private int currentProgress;

    private int rewardAmount;

    private String rewardType;

    private boolean isClaimed;

    private LocalDate date;

    // Helper para saber si está completa
    public boolean isCompleted() {
        return currentProgress >= goalAmount;
    }
}
