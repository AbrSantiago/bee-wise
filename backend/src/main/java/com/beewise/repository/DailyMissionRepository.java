package com.beewise.repository;

import com.beewise.model.daily.DailyMission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DailyMissionRepository extends JpaRepository<DailyMission, Long> {
    List<DailyMission> findAllByDate(LocalDate date);
}
