package com.beewise.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SimpleMultipleChoiceExerciseDTO {
    private String question;
    private String answer;
    private List<String> options;
}