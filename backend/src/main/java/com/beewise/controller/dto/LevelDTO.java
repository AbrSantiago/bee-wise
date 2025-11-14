package com.beewise.controller.dto;

import com.beewise.model.Level;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LevelDTO {
    private Long level;
    private String name;
    private String iconUrl;

    public LevelDTO(Level level) {
        this.level = level.getId();
        this.name = level.getName();
        this.iconUrl = level.getIconUrl();
    }
}
