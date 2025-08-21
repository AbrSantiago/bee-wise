package com.beewise.service.impl;

import com.beewise.exception.InvalidIdException;
import com.beewise.exception.LessonNotFoundException;
import com.beewise.model.Lesson;
import com.beewise.repository.LessonRepository;
import com.beewise.service.LessonService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;

    @Autowired
    public LessonServiceImpl(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    @Override
    public Lesson getLessonById(Long lessonId) {
        if (lessonId == null || lessonId <= 0) {
            throw new InvalidIdException("Lesson id must be a positive number");
        }
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new LessonNotFoundException("Lesson with id " + lessonId + " not found"));
    }
}
