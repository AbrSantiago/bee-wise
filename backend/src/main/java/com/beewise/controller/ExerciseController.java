package com.beewise.controller;

import com.beewise.controller.dto.ExerciseDTO;
import com.beewise.controller.dto.SimpleMultipleChoiceExerciseDTO;
import com.beewise.controller.dto.SimpleOpenExerciseDTO;
import com.beewise.model.Exercise;
import com.beewise.service.ExerciseService;
import jakarta.validation.Valid;
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
    public ResponseEntity<ExerciseDTO> createOpenExercise(@Valid @RequestBody SimpleOpenExerciseDTO dto) {
        Exercise exercise = exerciseService.createOpenExercise(dto);
        ExerciseDTO exerciseDTO = ExerciseDTO.fromExercise(exercise);
        return ResponseEntity.ok(exerciseDTO);
    }

    @PostMapping("/multipleChoice")
    public ResponseEntity<ExerciseDTO> createMultipleChoiceExercise(@Valid @RequestBody SimpleMultipleChoiceExerciseDTO dto) {
        Exercise exercise = exerciseService.createMultipleChoiceExercise(dto);
        ExerciseDTO exerciseDTO = ExerciseDTO.fromExercise(exercise);
        return ResponseEntity.ok(exerciseDTO);
    }

    @PutMapping("/open/{id}")
    public ResponseEntity<ExerciseDTO> updateOpenExercise(@Valid
            @PathVariable Long id,
            @RequestBody SimpleOpenExerciseDTO dto
    ) {
        Exercise exercise = exerciseService.updateOpenExercise(id, dto);
        ExerciseDTO exerciseDTO = ExerciseDTO.fromExercise(exercise);
        return ResponseEntity.ok(exerciseDTO);
    }

    @PutMapping("/multipleChoice/{id}")
    public ResponseEntity<ExerciseDTO> updateMultipleChoiceExercise(@Valid
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
