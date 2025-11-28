package com.beewise.controller.dto;

import com.beewise.model.daily.MissionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DailyMissionUpdateDTO {

    private MissionType type;
    private int progressAmount;


}
