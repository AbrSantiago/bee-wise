package com.beewise.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class MultipleChoiceExercise extends Exercise {
    @ElementCollection
    private List<String> options;

    public MultipleChoiceExercise(String question, List<String> options, String answer) {
        this.setQuestion(question);
        this.setOptions(options);
        this.setAnswer(answer);
    }
}
