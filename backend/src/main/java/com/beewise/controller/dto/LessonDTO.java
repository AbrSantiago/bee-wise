package com.beewise.controller.dto;

import com.beewise.model.Exercise;
import com.beewise.model.Lesson;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
public class LessonDTO {
    private Long id;
    private List<Exercise> exercises;

    public LessonDTO(Lesson lesson){
        this.id = lesson.getId();
        this.exercises = lesson.getExercises();
    }


    public static LessonDTO fromLesson(Lesson lesson) {
        return new LessonDTO(lesson);
    }
}
