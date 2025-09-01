package com.beewise.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OpenExerciseDTO extends ExerciseDTO {
    private String answer;
}
