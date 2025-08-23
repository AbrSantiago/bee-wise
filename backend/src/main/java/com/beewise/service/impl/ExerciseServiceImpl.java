package com.beewise.service.impl;

import com.beewise.controller.dto.SimpleExerciseDTO;
import com.beewise.exception.ExerciseNotFoundException;
import com.beewise.exception.InvalidIdException;
import com.beewise.model.Exercise;
import com.beewise.repository.ExerciseRepository;
import com.beewise.service.ExerciseService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ExerciseServiceImpl implements ExerciseService {
    private final ExerciseRepository repository;

    public ExerciseServiceImpl(ExerciseRepository repository) {
        this.repository = repository;
    }

    @Override
    public Exercise getExercise(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidIdException("Lesson id must be a positive number");
        }
        return repository.findById(id)
                .orElseThrow(() -> new ExerciseNotFoundException("Exercise with id" + id + "not found"));
    }

    @Override
    public Exercise createExercise(SimpleExerciseDTO simpleExerciseDTO) {
        Exercise exercise = new Exercise(simpleExerciseDTO.getQuestion(), simpleExerciseDTO.getAnswer());
        return repository.save(exercise);
    }

    @Override
    public Exercise updateExercise(Long id, SimpleExerciseDTO simpleExerciseDTO) {
        Exercise exercise = repository.findById(id)
                .orElseThrow(() -> new ExerciseNotFoundException("Exercise with id" + id + "not found"));

        exercise.setQuestion(simpleExerciseDTO.getQuestion());
        exercise.setAnswer(simpleExerciseDTO.getAnswer());

        return repository.save(exercise);
    }

    @Override
    public void deleteExercise(Long id) {
        repository.deleteById(id);
    }
}
