package com.beewise.service.impl;

import com.beewise.model.Lesson;
import com.beewise.model.LessonProgress;
import com.beewise.model.User;
import com.beewise.repository.LessonProgressRepository;
import com.beewise.service.LessonProgressService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class LessonProgressServiceImpl implements LessonProgressService {
    private final LessonProgressRepository progressRepository;

    public LessonProgressServiceImpl(LessonProgressRepository repository){
        this.progressRepository = repository;
    }

    @Override
    public void upsertProgress(User user, Lesson lesson) {
        LessonProgress progress = progressRepository.findByUser_IdAndLesson_Id(user.getId(), lesson.getId())
                .orElseGet(() -> new LessonProgress(user, lesson));
        progress.setAttempts(+1);
        progressRepository.save(progress);
    }
}
