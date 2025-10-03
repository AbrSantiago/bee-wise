package com.beewise.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserPointsDTO {
    private Long userId;
    private String username;
    private Integer points;
    private Integer currentLesson;

    public UserPointsDTO() {

    }
}