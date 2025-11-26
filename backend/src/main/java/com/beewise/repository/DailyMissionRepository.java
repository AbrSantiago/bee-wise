package com.beewise.repository;

import com.beewise.model.daily.DailyMission;
import com.beewise.model.daily.MissionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DailyMissionRepository extends JpaRepository<DailyMission, Long> {
    List<DailyMission> findByUserIdAndDate(Long userId, LocalDate date);
    List<DailyMission> findByUserIdAndDateAndType(Long userId, LocalDate date, MissionType type);
}
