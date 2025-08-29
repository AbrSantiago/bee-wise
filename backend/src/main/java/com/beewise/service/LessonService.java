package com.beewise.service;

import com.beewise.controller.dto.SimpleLessonDTO;
import com.beewise.model.Lesson;

public interface LessonService {
    Lesson getLessonById(Long lessonId);
    Lesson createLesson(SimpleLessonDTO lessonDTO);
    Lesson updateLesson(Long id, SimpleLessonDTO lessonDTO);
    Lesson addExercise(Long lessonId, Long exerciseId);
    Lesson removeExercise(Long lessonId, Long exerciseId);
    void deleteLesson(Long id);
}
