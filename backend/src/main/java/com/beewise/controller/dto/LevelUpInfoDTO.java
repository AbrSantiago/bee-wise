package com.beewise.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LevelUpInfoDTO {
    private Long oldLevelId;
    private Long newLevelId;
    private String newLevelName;
    private String newLevelIconUrl;
}