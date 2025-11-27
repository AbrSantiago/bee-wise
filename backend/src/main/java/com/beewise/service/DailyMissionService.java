package com.beewise.service;

import com.beewise.model.daily.DailyMissionProgress;
import com.beewise.model.daily.MissionType;

import java.util.List;

public interface DailyMissionService {
    List<DailyMissionProgress> getDailyMissions(String username);
//    void updateProgress(String username, DailyMissionProgress progress, int amountToAdd);
//    void claimReward(Long missionId);
}
