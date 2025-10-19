package com.beewise.service;

import com.beewise.controller.dto.SimpleMultipleChoiceExerciseDTO;
import com.beewise.controller.dto.SimpleOpenExerciseDTO;
import com.beewise.model.Exercise;
import com.beewise.model.ExerciseCategory;

import java.util.List;

public interface ExerciseService {
    Exercise getExercise(Long id);
    Exercise createOpenExercise(SimpleOpenExerciseDTO dto);
    Exercise updateOpenExercise(Long id, SimpleOpenExerciseDTO dto);
    List<Exercise> createOpenExercises(List<SimpleOpenExerciseDTO> dto);
    Exercise createMultipleChoiceExercise(SimpleMultipleChoiceExerciseDTO dto);
    Exercise updateMultipleChoiceExercise(Long id, SimpleMultipleChoiceExerciseDTO dto);
    void deleteExercise(Long id);
    List<Exercise> getRandomExercises(int limit, ExerciseCategory category);
}
