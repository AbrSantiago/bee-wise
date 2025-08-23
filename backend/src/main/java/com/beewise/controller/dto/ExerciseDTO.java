package com.beewise.controller.dto;

import com.beewise.model.Exercise;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExerciseDTO {
    private Long id;
    private String question;
    private String answer;

    public ExerciseDTO(Exercise exercise) {
        this.id = exercise.getId();
        this.question = exercise.getQuestion();
        this.answer = exercise.getAnswer();
    }

    public static ExerciseDTO fromExercise(Exercise exercise) {
        return new ExerciseDTO(exercise);
    }
}
