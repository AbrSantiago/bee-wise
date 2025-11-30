package com.beewise.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LessonCompleteRequestDTO {
    private Long completedLessonId;
    private Long userId;
    private int correctExercises;
}
