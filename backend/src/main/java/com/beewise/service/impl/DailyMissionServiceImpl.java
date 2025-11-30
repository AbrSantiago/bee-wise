package com.beewise.service.impl;

import com.beewise.controller.dto.DailyMissionUpdateDTO;
import com.beewise.controller.dto.DailyMissionUpdateOutDTO;
import com.beewise.model.User;
import com.beewise.model.daily.DailyMission;
import com.beewise.model.daily.DailyMissionProgress;
import com.beewise.repository.DailyMissionProgressRepository;
import com.beewise.repository.DailyMissionRepository;
import com.beewise.service.DailyMissionService;
import com.beewise.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DailyMissionServiceImpl implements DailyMissionService {
    private final DailyMissionRepository dailyMissionRepository;
    private final UserService userService;
    private final DailyMissionProgressRepository progressRepository;

    public DailyMissionServiceImpl(
            DailyMissionRepository dailyMissionRepository,
            UserService userService, DailyMissionProgressRepository progressRepository) {
        this.dailyMissionRepository = dailyMissionRepository;
        this.userService = userService;
        this.progressRepository = progressRepository;
    }

    @Override
    public List<DailyMissionProgress> getDailyMissions(String username) {
        LocalDate today = LocalDate.now();
        User user = userService.getUserByUsername(username);
        List<DailyMission> missions = dailyMissionRepository.findAllByDate(today);
        if (missions.isEmpty()) {
            missions = generateMissions(today);
        }
        List<DailyMissionProgress> progresses = progressRepository.findAllByUserAndMissionIn(user, missions);
        if (progresses.isEmpty()) {
            progresses = generateProgressesFor(user, missions);
        }
        return progresses;
    }

    @Override
    public List<DailyMissionUpdateOutDTO> updateProgress(String username, List<DailyMissionUpdateDTO> progressUpdateDTOS) {
        User user = userService.getUserByUsername(username);
        List<DailyMissionUpdateOutDTO> missions = new ArrayList<>();

        for (DailyMissionUpdateDTO progressUpdate : progressUpdateDTOS) {
            DailyMissionUpdateOutDTO mission = new DailyMissionUpdateOutDTO();
            Optional<DailyMissionProgress> progressOp = progressRepository.findByUserAndMission_TypeAndMission_Date(user, progressUpdate.getType(), LocalDate.now());

            if (progressOp.isEmpty()){
                mission.setWasUpdated(false);
            } else if (progressOp.get().isCompleted()) {
                DailyMissionProgress progress = progressOp.get();
                mission = new DailyMissionUpdateOutDTO(progress);
                mission.setCurrentProgress(mission.getPreviousProgress());
                mission.setWasUpdated(false);
            } else {
                DailyMissionProgress progress = progressOp.get();
                mission = new DailyMissionUpdateOutDTO(progress);
                progress.updateProgress(progressUpdate.getProgressAmount());
                mission.addProgress(progressUpdate.getProgressAmount());
                mission.setWasUpdated(true);

                if (progress.isCompleted()) {
                    user.addBeeCoins(progress.getReward());
                    progress.setClaimed(true);
                    mission.setClaimed(true);
                }

                progressRepository.save(progress);
            }
            missions.add(mission);
        }
        return missions;
    }

    // ================ HELPERS ================

    private List<DailyMissionProgress> generateProgressesFor(User user, List<DailyMission> missions) {
        List<DailyMissionProgress> progresses = new ArrayList<>();
        for (DailyMission mission : missions) {
            DailyMissionProgress progress = new DailyMissionProgress();
            progress.setUser(user);
            progress.setMission(mission);
            progresses.add(progress);
        }
        return progressRepository.saveAll(progresses);
    }

    public List<DailyMission> generateMissions(LocalDate date) {
        List<DailyMission> newMissions = DailyMission.get2RandomDailyMission(date);
        return dailyMissionRepository.saveAll(newMissions);
    }
}
