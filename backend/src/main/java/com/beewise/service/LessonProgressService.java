package com.beewise.service;

import com.beewise.model.Lesson;
import com.beewise.model.User;

public interface LessonProgressService {
    void upsertProgress(User user, Lesson lesson);
}
