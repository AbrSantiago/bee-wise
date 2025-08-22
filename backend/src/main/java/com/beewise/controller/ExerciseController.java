package com.beewise.controller;

import com.beewise.controller.dto.ExerciseDTO;
import com.beewise.controller.dto.SimpleExerciseDTO;
import com.beewise.model.Exercise;
import com.beewise.service.ExerciseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exercise")
public class ExerciseController {
    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExerciseDTO> getExercise(@PathVariable Long id) {
        Exercise exercise = exerciseService.getExercise(id);
        ExerciseDTO exerciseDTO = ExerciseDTO.fromExercise(exercise);
        return ResponseEntity.ok(exerciseDTO);
    }

    @PostMapping
    public ResponseEntity<ExerciseDTO> createExercise(@RequestBody SimpleExerciseDTO simpleExerciseDTO) {
        Exercise exercise = exerciseService.createExercise(simpleExerciseDTO);
        ExerciseDTO exerciseDTO = ExerciseDTO.fromExercise(exercise);
        return ResponseEntity.ok(exerciseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExerciseDTO> updateExercise(
            @PathVariable Long id,
            @RequestBody SimpleExerciseDTO simpleExerciseDTO
    ) {
        Exercise exercise = exerciseService.updateExercise(id, simpleExerciseDTO);
        ExerciseDTO exerciseDTO = ExerciseDTO.fromExercise(exercise);
        return ResponseEntity.ok(exerciseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable Long id) {
        exerciseService.deleteExercise(id);
        return ResponseEntity.noContent().build();
    }
}
