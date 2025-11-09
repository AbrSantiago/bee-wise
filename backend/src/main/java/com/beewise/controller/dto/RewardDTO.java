package com.beewise.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class RewardDTO {
    private int pointsGained;
    private int beeCoinsGained;
    private ShopItemDTO itemGained;
}
