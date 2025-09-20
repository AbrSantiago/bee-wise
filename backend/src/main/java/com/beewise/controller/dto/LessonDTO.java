package com.beewise.controller.dto;

import com.beewise.model.Lesson;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class LessonDTO {
    private Long id;
    private String title;
    private String description;
    private List<ExerciseDTO> exercises;

    public LessonDTO(Lesson lesson){
        this.id = lesson.getId();
        this.title = lesson.getTitle();
        this.description = lesson.getDescription();
        this.exercises = lesson.getExercises()
                .stream()
                .map(ExerciseDTO::fromExercise)
                .toList();
    }

    public static LessonDTO fromLesson(Lesson lesson) {
        return new LessonDTO(lesson);
    }
}
