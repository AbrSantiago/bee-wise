package com.beewise.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class OpenExercise extends Exercise {
    public OpenExercise(String question, String answer, ExerciseCategory category) {
        this.setQuestion(question);
        this.setAnswer(answer);
        this.setCategory(category);
    }

    @Override
    public void setOptions(List<String> options) {

    }
}
