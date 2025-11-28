package com.beewise.repository;

import com.beewise.model.User;
import com.beewise.model.daily.DailyMission;
import com.beewise.model.daily.DailyMissionProgress;
import com.beewise.model.daily.MissionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyMissionProgressRepository extends JpaRepository<DailyMissionProgress, Long> {
    List<DailyMissionProgress> findAllByUserAndMissionIn(User user, List<DailyMission> missions);
    Optional<DailyMissionProgress> findByUserAndMissionType(User user, MissionType type);
    Optional<DailyMissionProgress> findByUserAndMission_TypeAndMission_Date(
            User user,
            MissionType type,
            LocalDate date
    );
}
