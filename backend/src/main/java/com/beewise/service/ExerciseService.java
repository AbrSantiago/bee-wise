package com.beewise.service;

import com.beewise.controller.dto.SimpleExerciseDTO;
import com.beewise.model.Exercise;

public interface ExerciseService {
    Exercise getExercise(Long id);
    Exercise createExercise(SimpleExerciseDTO simpleExerciseDTO);
    Exercise updateExercise(Long id, SimpleExerciseDTO simpleExerciseDTO);
    void deleteExercise(Long id);
}
