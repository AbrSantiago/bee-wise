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
    public OpenExercise(String question, String answer) {
        this.setQuestion(question);
        this.setAnswer(answer);
    }

    @Override
    public void setOptions(List<String> options) {

    }
}
