package com.beewise.service.impl;

import com.beewise.controller.dto.*;
import com.beewise.exception.ExerciseNotFoundException;
import com.beewise.exception.InvalidIdException;
import com.beewise.model.Exercise;
import com.beewise.model.MultipleChoiceExercise;
import com.beewise.model.OpenExercise;
import com.beewise.repository.ExerciseRepository;
import com.beewise.service.ExerciseService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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
            throw new InvalidIdException("Exercise id must be a positive number");
        }
        return repository.findById(id)
                .orElseThrow(() -> new ExerciseNotFoundException("Exercise with id " + id + " not found"));
    }

    @Override
    public Exercise createOpenExercise(SimpleOpenExerciseDTO dto) {
        Exercise exercise = new OpenExercise(dto.getQuestion(), dto.getAnswer());
        return repository.save(exercise);
    }

    @Override
    public Exercise createMultipleChoiceExercise(SimpleMultipleChoiceExerciseDTO dto) {
        Exercise exercise = new MultipleChoiceExercise(dto.getQuestion(), dto.getOptions(), dto.getAnswer());
        return repository.save(exercise);
    }

    @Override
    public Exercise updateOpenExercise(Long id, SimpleOpenExerciseDTO dto) {
        Exercise exercise = repository.findById(id)
                .orElseThrow(() -> new ExerciseNotFoundException("Exercise with id " + id + " not found"));
        if (!(exercise instanceof OpenExercise)) {
            throw new InvalidIdException("Exercise with id " + id + " is not Open");
        }
        exercise.setQuestion(dto.getQuestion());
        exercise.setAnswer(dto.getAnswer());

        return repository.save(exercise);
    }

    @Override
    public Exercise updateMultipleChoiceExercise(Long id, SimpleMultipleChoiceExerciseDTO dto) {
        Exercise exercise = repository.findById(id)
                .orElseThrow(() -> new ExerciseNotFoundException("Exercise with id " + id + " not found"));
        if (!(exercise instanceof MultipleChoiceExercise)) {
            throw new InvalidIdException("Exercise with id " + id + " is not Multiple-Choice");
        }
        exercise.setQuestion(dto.getQuestion());
        exercise.setOptions(dto.getOptions());
        exercise.setAnswer(dto.getAnswer());

        return repository.save(exercise);
    }

    @Override
    public void deleteExercise(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Exercise> getRandomExercises(int limit) {
        Pageable pageable = PageRequest.of(0, 5);
        return repository.findRandomExercises(pageable);
    }
}
