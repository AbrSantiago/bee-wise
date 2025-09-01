package com.beewise.service.impl;

import com.beewise.controller.dto.SimpleLessonDTO;
import com.beewise.exception.ExerciseNotFoundException;
import com.beewise.exception.InvalidIdException;
import com.beewise.exception.LessonNotFoundException;
import com.beewise.model.Exercise;
import com.beewise.model.Lesson;
import com.beewise.repository.ExerciseRepository;
import com.beewise.repository.LessonRepository;
import com.beewise.service.LessonService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;
    private final ExerciseRepository exerciseRepository;

    @Autowired
    public LessonServiceImpl(LessonRepository lessonRepository, ExerciseRepository exerciseRepository) {
        this.lessonRepository = lessonRepository;
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public Lesson getLessonById(Long lessonId) {
        if (lessonId == null || lessonId <= 0) {
            throw new InvalidIdException("Lesson id must be a positive number");
        }
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new LessonNotFoundException("Lesson with id " + lessonId + " not found"));
    }

    @Override
    public Lesson createLesson(SimpleLessonDTO lessonDTO) {
        Lesson lesson = new Lesson(lessonDTO.getTitle(), lessonDTO.getDescription());
        return lessonRepository.save(lesson);
    }

    @Override
    public Lesson updateLesson(Long id, SimpleLessonDTO lessonDTO) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new LessonNotFoundException("Lesson with id " + id + " not found"));

        lesson.setTitle(lessonDTO.getTitle());
        lesson.setDescription(lessonDTO.getDescription());

        return lessonRepository.save(lesson);
    }

    @Override
    public Lesson addExercise(Long lessonId, Long exerciseId) {
        if (lessonId == null || lessonId <= 0) {
            throw new InvalidIdException("Lesson id must be a positive number");
        }
        if (exerciseId == null || exerciseId <= 0) {
            throw new InvalidIdException("Exercise id must be a positive number");
        }
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new LessonNotFoundException("Lesson with id " + lessonId + " not found"));
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ExerciseNotFoundException("Exercise with id " + exerciseId + " not found"));

        List<Exercise> exercises = lesson.getExercises();
        if (exercises.stream().anyMatch(e -> Objects.equals(e.getId(), exerciseId))){
            throw new InvalidIdException("Exercise with id " + exerciseId + " is already in the lesson");
        }
        exercises.add(exercise);

        exercise.setLesson(lesson);
        exerciseRepository.save(exercise);

        return lessonRepository.save(lesson);
    }

    @Override
    public Lesson removeExercise(Long lessonId, Long exerciseId) {
        if (lessonId == null || lessonId <= 0) {
            throw new InvalidIdException("Lesson id must be a positive number");
        }
        if (exerciseId == null || exerciseId <= 0) {
            throw new InvalidIdException("Exercise id must be a positive number");
        }
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new LessonNotFoundException("Lesson with id " + lessonId + " not found"));
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ExerciseNotFoundException("Exercise with id " + exerciseId + " not found"));

        List<Exercise> exercises = lesson.getExercises();
        if (exercises.stream().noneMatch(e -> Objects.equals(e.getId(), exerciseId))){
            throw new InvalidIdException("Exercise with id " + exerciseId + " is not in the lesson");
        }
        exercises.remove(exercise);

        exercise.setLesson(null);
        exerciseRepository.save(exercise);

        return lessonRepository.save(lesson);
    }

    @Override
    public void deleteLesson(Long id) {
        lessonRepository.deleteById(id);
    }
}
