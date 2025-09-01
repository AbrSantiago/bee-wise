package com.beewise.service;

import com.beewise.controller.dto.SimpleLessonDTO;
import com.beewise.model.Lesson;

import java.util.List;

public interface LessonService {
    Lesson getLessonById(Long lessonId);
    List<Lesson> getAllLessons();
    Lesson createLesson(SimpleLessonDTO lessonDTO);
    Lesson updateLesson(Long id, SimpleLessonDTO lessonDTO);
    void deleteLesson(Long id);
}
