package com.beewise.service.impl;

import com.beewise.exception.AnswerWrongRolException;
import com.beewise.exception.MissionAlreadyClaimedException;
import com.beewise.exception.MissionNotCompletedException;
import com.beewise.model.User;
import com.beewise.model.daily.DailyMission;
import com.beewise.model.daily.MissionType;
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

@Service
@Transactional
public class DailyMissionServiceImpl implements DailyMissionService {

    private final DailyMissionRepository dailyMissionRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    public DailyMissionServiceImpl(DailyMissionRepository dailyMissionRepository, UserService userService, UserRepository userRepository) {
        this.dailyMissionRepository = dailyMissionRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public List<DailyMission> getDailyMissions(Long userId) {
        LocalDate today = LocalDate.now();
        List<DailyMission> missions = dailyMissionRepository.findByUserIdAndDate(userId, today);

        if (missions.isEmpty()) {
            return generateMissionsForUser(userId, today);
        }
        return missions;
    }

    @Override
    public List<DailyMission> generateMissionsForUser(Long userId, LocalDate date) {
        User user = userRepository.findById(userId).orElseThrow();
        List<DailyMission> newMissions = new ArrayList<>();

        // Ejemplo: Crear 3 misiones fijas o aleatorias
        // Misión 1: Jugar 1 Challenge
        DailyMission m1 = new DailyMission();
        m1.setUser(user);
        m1.setDate(date);
        m1.setType(MissionType.PLAY_CHALLENGE);
        m1.setGoalAmount(1);
        m1.setRewardAmount(20);
        m1.setRewardType("BEECOINS");
        newMissions.add(m1);

        // Misión 2: Ganar 50 BeeCoins
        DailyMission m2 = new DailyMission();
        m2.setUser(user);
        m2.setDate(date);
        m2.setType(MissionType.EARN_BEECOINS);
        m2.setGoalAmount(50);
        m2.setRewardAmount(10); // Premio en puntos quizás?
        m2.setRewardType("POINTS");
        newMissions.add(m2);

        return dailyMissionRepository.saveAll(newMissions);
    }

    @Override
    public void updateProgress(Long userId, MissionType type, int amountToAdd) {
        LocalDate today = LocalDate.now();
        List<DailyMission> missions = dailyMissionRepository.findByUserIdAndDateAndType(userId, today, type);

        for (DailyMission mission : missions) {
            if (!mission.isCompleted()) {
                mission.setCurrentProgress(mission.getCurrentProgress() + amountToAdd);
                // Opcional: Limitar al máximo
                if (mission.getCurrentProgress() > mission.getGoalAmount()) {
                    mission.setCurrentProgress(mission.getGoalAmount());
                }
                dailyMissionRepository.save(mission);
            }
        }
    }

    @Override
    public void claimReward(Long missionId) {
        // 1. Buscar la misión
        DailyMission mission = dailyMissionRepository.findById(missionId)
                .orElseThrow(() -> new EntityNotFoundException("Mission not found with id: " + missionId));

        // 2. Seguridad: Verificar que la misión pertenece al usuario que llama
        if (!mission.getUser().getUsername().equals(username)) {
            throw new AnswerWrongRolException("This mission does not belong to you");
        }

        // 3. Validar estado: ¿Ya está completa?
        if (!mission.isCompleted()) {
            throw new MissionNotCompletedException("La misión aún no está completa. Progreso: " + mission.getCurrentProgress() + "/" + mission.getGoalAmount());
        }

        // 4. Validar estado: ¿Ya fue reclamada?
        if (mission.isClaimed()) {
            throw new MissionAlreadyClaimedException("Esta recompensa ya fue reclamada.");
        }

        // 5. Entregar la recompensa
        User user = mission.getUser();
        String rewardType = mission.getRewardType(); // "BEECOINS" o "POINTS"
        int amount = mission.getRewardAmount();

        if ("BEECOINS".equalsIgnoreCase(rewardType)) {
            // Usamos tu método helper que NO guarda
            userService.addBeeCoins(user, amount);
            // ¡IMPORTANTE! Como addBeeCoins no guarda, debemos guardar explícitamente al usuario aquí
            userRepository.save(user);

        } else if ("POINTS".equalsIgnoreCase(rewardType)) {
            // Usamos tu método que SÍ guarda y recalcula nivel
            boolean leveledUp = userService.addPointsToUser(user, amount);
            // (Aquí podrías agregar lógica si quieres notificar el levelUp en la respuesta)
        }

        // 6. Marcar como reclamada y guardar misión
        mission.setClaimed(true);
        return dailyMissionRepository.save(mission);
    }
    }
}
