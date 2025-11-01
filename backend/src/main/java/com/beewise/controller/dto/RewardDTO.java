package com.beewise.controller.dto;

import com.beewise.model.Reward;
import com.beewise.model.ShopItem;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@NoArgsConstructor
@Setter
@Getter
public class RewardDTO {

    private int pointsGained;
    private int beeCoinsGained;
    private ShopItemDTO itemGained;

    public RewardDTO(Reward reward) {
        this.pointsGained = reward.getPointsGained();
        this.beeCoinsGained = reward.getBeeCoinsGained();
        this.itemGained = Optional.ofNullable(reward.getItemGained()).map(ShopItemDTO::new).orElse(null);
    }
}
