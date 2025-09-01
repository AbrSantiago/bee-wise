package com.beewise.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MultipleChoiceExerciseDTO extends ExerciseDTO {
    private List<String> options;
    private String answer;
}
