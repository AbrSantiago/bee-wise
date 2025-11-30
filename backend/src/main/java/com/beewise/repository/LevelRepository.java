package com.beewise.repository;

import com.beewise.model.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LevelRepository extends JpaRepository<Level, Integer> {

    Optional<Level> findFirstByMinPointsLessThanEqualOrderByMinPointsDesc(int userPoints);
    Optional<Level> findFirstByOrderByMinPointsAsc();
}