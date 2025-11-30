package com.beewise.controller.dto;

import com.beewise.model.Exercise;
import com.beewise.model.ExerciseCategory;
import com.beewise.model.MultipleChoiceExercise;
import com.beewise.model.OpenExercise;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class ExerciseDTO {
    private Long id;
    private String question;
    private String type;
    private ExerciseCategory category;

    public static ExerciseDTO fromExercise(Exercise exercise) {
        if (exercise instanceof OpenExercise open) {
            OpenExerciseDTO dto = new OpenExerciseDTO();
            dto.setId(open.getId());
            dto.setQuestion(open.getQuestion());
            dto.setType("OPEN");
            dto.setCategory(open.getCategory());
            dto.setAnswer(open.getAnswer());
            return dto;
        } else if (exercise instanceof MultipleChoiceExercise mc) {
            MultipleChoiceExerciseDTO dto = new MultipleChoiceExerciseDTO();
            dto.setId(mc.getId());
            dto.setQuestion(mc.getQuestion());
            dto.setType("MULTIPLE_CHOICE");
            dto.setCategory(mc.getCategory());
            dto.setOptions(mc.getOptions());
            dto.setAnswer(mc.getAnswer());
            return dto;
        }
        throw new IllegalArgumentException("Unknown exercise type");
    }
}
