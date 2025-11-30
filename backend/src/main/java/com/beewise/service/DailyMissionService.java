package com.beewise.service;

import com.beewise.controller.dto.DailyMissionUpdateDTO;
import com.beewise.controller.dto.DailyMissionUpdateOutDTO;
import com.beewise.model.daily.DailyMissionProgress;

import java.util.List;

public interface DailyMissionService {
    List<DailyMissionProgress> getDailyMissions(String username);
    List<DailyMissionUpdateOutDTO> updateProgress(String username, List<DailyMissionUpdateDTO> missionUpdateDTOS);
}
