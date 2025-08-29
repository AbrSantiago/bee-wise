package com.beewise.controller;

import com.beewise.controller.dto.ExerciseDTO;
import com.beewise.controller.dto.SimpleExerciseDTO;
import com.beewise.controller.dto.SimpleMultipleChoiceExerciseDTO;
import com.beewise.controller.dto.SimpleOpenExerciseDTO;
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

    @PostMapping("/open")
    public ResponseEntity<ExerciseDTO> createOpenExercise(@RequestBody SimpleOpenExerciseDTO dto) {
        Exercise exercise = exerciseService.createOpenExercise(dto);
        ExerciseDTO exerciseDTO = ExerciseDTO.fromExercise(exercise);
        return ResponseEntity.ok(exerciseDTO);
    }

    @PostMapping("/multiple choice")
    public ResponseEntity<ExerciseDTO> createMultipleChoiceExercise(@RequestBody SimpleMultipleChoiceExerciseDTO dto) {
        Exercise exercise = exerciseService.createMultipleChoiceExercise(dto);
        ExerciseDTO exerciseDTO = ExerciseDTO.fromExercise(exercise);
        return ResponseEntity.ok(exerciseDTO);
    }

    @PutMapping("/open/{id}")
    public ResponseEntity<ExerciseDTO> updateOpenExercise(
            @PathVariable Long id,
            @RequestBody SimpleOpenExerciseDTO dto
    ) {
        Exercise exercise = exerciseService.updateOpenExercise(id, dto);
        ExerciseDTO exerciseDTO = ExerciseDTO.fromExercise(exercise);
        return ResponseEntity.ok(exerciseDTO);
    }

    @PutMapping("/multiple choice/{id}")
    public ResponseEntity<ExerciseDTO> updateMultipleChoiceExercise(
            @PathVariable Long id,
            @RequestBody SimpleMultipleChoiceExerciseDTO dto
    ) {
        Exercise exercise = exerciseService.updateMultipleChoiceExercise(id, dto);
        ExerciseDTO exerciseDTO = ExerciseDTO.fromExercise(exercise);
        return ResponseEntity.ok(exerciseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable Long id) {
        exerciseService.deleteExercise(id);
        return ResponseEntity.noContent().build();
    }
}
