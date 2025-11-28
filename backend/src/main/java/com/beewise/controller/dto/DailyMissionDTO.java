package com.beewise.controller.dto;

import com.beewise.model.daily.DailyMissionProgress;
import com.beewise.model.daily.MissionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class DailyMissionDTO {
    private MissionType type;
    private int goalAmount;
    private int currentProgress;
    private int rewardAmount;
    private boolean isClaimed;
    private LocalDate date;

    public DailyMissionDTO(DailyMissionProgress mission) {
        this.type = mission.getMission().getType();
        this.goalAmount = mission.getMission().getGoalAmount();
        this.currentProgress = mission.getCurrentProgress();
        this.rewardAmount = mission.getMission().getRewardAmount();
        this.isClaimed = mission.isClaimed();
        this.date = mission.getMission().getDate();
    }
}
