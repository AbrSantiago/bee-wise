package com.beewise.controller.dto;

import com.beewise.model.Lesson;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SimpleLessonDTO {
    private Long id;
    private String title;
    private String description;

    public SimpleLessonDTO(Lesson lesson){
        this.id = lesson.getId();
        this.title = lesson.getTitle();
        this.description = lesson.getDescription();
    }

    public static List<SimpleLessonDTO> fromLessonList(List<Lesson> lessons) {
        return lessons.stream().map(SimpleLessonDTO::new).toList();
    }
}
