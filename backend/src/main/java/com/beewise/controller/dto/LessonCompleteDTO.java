package com.beewise.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LessonCompleteDTO {
    private boolean success;
    private String message;
    private int totalPoints;
}
