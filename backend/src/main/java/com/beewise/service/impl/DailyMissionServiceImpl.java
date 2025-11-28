package com.beewise.service.impl;

import com.beewise.controller.dto.DailyMissionUpdateDTO;
import com.beewise.controller.dto.DailyMissionUpdateOutDTO;
import com.beewise.exception.AnswerWrongRolException;
import com.beewise.exception.MissionAlreadyClaimedException;
import com.beewise.exception.MissionDoesNotExistException;
import com.beewise.exception.MissionNotCompletedException;
import com.beewise.model.User;
import com.beewise.model.challenge.CompletedState;
import com.beewise.model.challenge.WaitingChallengedState;
import com.beewise.model.challenge.WaitingChallengerState;
import com.beewise.model.daily.DailyMission;
import com.beewise.model.daily.DailyMissionProgress;
import com.beewise.model.daily.MissionType;
import com.beewise.repository.DailyMissionProgressRepository;
import com.beewise.repository.DailyMissionRepository;
import com.beewise.repository.UserRepository;
import com.beewise.service.DailyMissionService;
import com.beewise.service.UserService;
import jakarta.persistence.EntityNotFoundException;
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
    private final UserRepository userRepository;
    private final DailyMissionProgressRepository progressRepository;

    public DailyMissionServiceImpl(
            DailyMissionRepository dailyMissionRepository,
            UserService userService, UserRepository userRepository, DailyMissionProgressRepository progressRepository) {
        this.dailyMissionRepository = dailyMissionRepository;
        this.userService = userService;
        this.userRepository = userRepository;
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
    public DailyMissionUpdateOutDTO updateProgress(String username, DailyMissionUpdateDTO progressUpdate) {
        User user = userService.getUserByUsername(username);

        DailyMissionUpdateOutDTO mission = new DailyMissionUpdateOutDTO();
        Optional<DailyMissionProgress> progressOp = progressRepository.findByUserAndMission_TypeAndMission_Date(user, progressUpdate.getType(), LocalDate.now());

        if (progressOp.isEmpty() || progressOp.get().isCompleted()){
            mission.setWasUpdated(false);
        } else {
            DailyMissionProgress progress = progressOp.get();
            mission = new DailyMissionUpdateOutDTO(progress);
            progress.updateProgress(progressUpdate.getProgressAmount());
            mission.addProgress(progressUpdate.getProgressAmount());
            mission.setWasUpdated(true);
            progressRepository.save(progress);
        }
        return mission;

//        if (!missionProgress.isClaimed()){
//            int newProgress = missionProgress.getCurrentProgress() + amountToAdd;
//            int goalAmount = missionProgress.getMission().getGoalAmount();
//
//            if (newProgress > goalAmount){
//                newProgress = goalAmount;
//            }
//
//            missionProgress.setCurrentProgress(newProgress);
//            progressRepository.save(missionProgress);
//        }
    }
//
//    @Override
//    public void claimReward(Long missionId) {
//        // 1. Buscar la misión
//        DailyMission mission = dailyMissionRepository.findById(missionId)
//                .orElseThrow(() -> new EntityNotFoundException("Mission not found with id: " + missionId));
//
//        // 2. Seguridad: Verificar que la misión pertenece al usuario que llama
//        if (!mission.getUser().getUsername().equals(username)) {
//            throw new AnswerWrongRolException("This mission does not belong to you");
//        }
//
//        // 3. Validar estado: ¿Ya está completa?
//        if (!mission.isCompleted()) {
//            throw new MissionNotCompletedException("La misión aún no está completa. Progreso: " + mission.getCurrentProgress() + "/" + mission.getGoalAmount());
//        }
//
//        // 4. Validar estado: ¿Ya fue reclamada?
//        if (mission.isClaimed()) {
//            throw new MissionAlreadyClaimedException("Esta recompensa ya fue reclamada.");
//        }
//
//        // 5. Entregar la recompensa
//        User user = mission.getUser();
//        String rewardType = mission.getRewardType(); // "BEECOINS" o "POINTS"
//        int amount = mission.getRewardAmount();
//
//        if ("BEECOINS".equalsIgnoreCase(rewardType)) {
//            // Usamos tu método helper que NO guarda
//            userService.addBeeCoins(user, amount);
//            // ¡IMPORTANTE! Como addBeeCoins no guarda, debemos guardar explícitamente al usuario aquí
//            userRepository.save(user);
//
//        } else if ("POINTS".equalsIgnoreCase(rewardType)) {
//            // Usamos tu método que SÍ guarda y recalcula nivel
//            boolean leveledUp = userService.addPointsToUser(user, amount);
//            // (Aquí podrías agregar lógica si quieres notificar el levelUp en la respuesta)
//        }
//
//        // 6. Marcar como reclamada y guardar misión
//        mission.setClaimed(true);
//        return dailyMissionRepository.save(mission);
//
//    }

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
