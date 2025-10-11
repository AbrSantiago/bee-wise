package com.beewise.controller.dto;

import com.beewise.model.ExerciseCategory;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SimpleOpenExerciseDTO {
    @NotBlank(message = "Question cannot be empty")
    private String question;

    @NotBlank(message = "Answer cannot be empty")
    private String answer;

    private ExerciseCategory category;
}
