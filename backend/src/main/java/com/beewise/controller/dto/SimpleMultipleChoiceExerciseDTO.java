package com.beewise.controller.dto;

import com.beewise.model.ExerciseCategory;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SimpleMultipleChoiceExerciseDTO {
    @NotBlank(message = "Question cannot be empty")
    private String question;

    @NotBlank(message = "Answer cannot be empty")
    private String answer;

    private List<@NotBlank String> options;

    @Enumerated(EnumType.STRING)
    private ExerciseCategory category;
}