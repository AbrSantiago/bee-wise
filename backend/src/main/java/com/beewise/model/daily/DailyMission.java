package com.beewise.model.daily;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Entity
@Data
@NoArgsConstructor
public class DailyMission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MissionType type;

    @Min(value = 1, message = "Goal must be positive")
    private int goalAmount;

    private int rewardAmount;

    private LocalDate date;

    public static List<DailyMission> get2RandomDailyMission(LocalDate date) {
        MissionType[] types = MissionType.getTwoRandomDifferent();
        List<DailyMission> missions = new ArrayList<>();
        for (MissionType type : types) {
            DailyMission mission = new DailyMission();
            mission.setType(type);
            mission.setGoalAmount(type.getGoalAmount());
            mission.setRewardAmount(randomReward());
            mission.setDate(date);
            missions.add(mission);
        }
        return missions;
    }

    private static final int[] REWARDS = {20, 25, 30};

    private static int randomReward() {
        return REWARDS[ThreadLocalRandom.current().nextInt(REWARDS.length)];
    }
}
