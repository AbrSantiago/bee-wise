package com.beewise.controller.dto;

import com.beewise.model.Lesson;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SimpleLessonDTO {
    private String title;
    private String description;

    public SimpleLessonDTO(Lesson lesson){
        this.title = lesson.getTitle();
        this.description = lesson.getDescription();
    }
}
