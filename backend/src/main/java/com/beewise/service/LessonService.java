package com.beewise.service;

import com.beewise.controller.dto.SimpleLessonDTO;
import com.beewise.controller.dto.LessonDTO;
import com.beewise.model.Lesson;

public interface LessonService {
    Lesson getLessonById(Long lessonId);
    Lesson createLesson(SimpleLessonDTO lessonDTO);
    Lesson updateLesson(Long id, SimpleLessonDTO lessonDTO);
    void deleteLesson(Long id);
}
