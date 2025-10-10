package com.beewise.repository;

import com.beewise.model.Exercise;
import com.beewise.model.ExerciseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    @Query("SELECT e FROM Exercise e WHERE e.category = :category ORDER BY function('random')")
    List<Exercise> findRandomExercisesByCategory(
            @Param("category") ExerciseCategory category,
            Pageable pageable);
}
