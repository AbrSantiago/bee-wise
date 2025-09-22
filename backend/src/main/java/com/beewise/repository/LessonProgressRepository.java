package com.beewise.repository;

import com.beewise.model.Lesson;
import com.beewise.model.LessonProgress;
import com.beewise.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LessonProgressRepository extends JpaRepository<LessonProgress, Long> {
    Optional<LessonProgress> findByUser_IdAndLesson_Id(Long userId, Long lessonId);
}
