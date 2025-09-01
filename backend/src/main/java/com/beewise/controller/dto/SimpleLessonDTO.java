package com.beewise.controller.dto;

import com.beewise.model.Lesson;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SimpleLessonDTO {
    private Long id;
    @NotBlank(message = "Title cannot be empty")
    @Size(max = 255, message = "Title too long")
    private String title;

    @Size(max = 1000, message = "Description too long")
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
