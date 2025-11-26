package com.beewise.service;

import com.beewise.model.daily.DailyMission;
import com.beewise.model.daily.MissionType;

import java.time.LocalDate;
import java.util.List;

public interface DailyMissionService {

    List<DailyMission> getDailyMissions(Long userId);
    List<DailyMission> generateMissionsForUser(Long userId, LocalDate date);
    void updateProgress(Long userId, MissionType type, int amountToAdd);
    void claimReward(Long missionId);
}
