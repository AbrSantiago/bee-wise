package com.beewise.repository;

import com.beewise.model.User;
import com.beewise.model.daily.DailyMission;
import com.beewise.model.daily.DailyMissionProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DailyMissionProgressRepository extends JpaRepository<DailyMissionProgress, Long> {
    List<DailyMissionProgress> findAllByUserAndMissionIn(User usern, List<DailyMission> missions);
}
