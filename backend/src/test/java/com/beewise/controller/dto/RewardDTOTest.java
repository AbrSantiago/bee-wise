package com.beewise.controller.dto;
import com.beewise.model.Reward;
import com.beewise.model.ShopItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RewardDTOTest {
    @Test
    void constructor_setsFieldsCorrectly_whenItemGainedIsPresent() {
        ShopItem item = new ShopItem();
        item.setId(1L);
        item.setName("Bee Hat");

        Reward reward = new Reward();
        reward.setPointsGained(100);
        reward.setBeeCoinsGained(50);
        reward.setItemGained(item);

        RewardDTO dto = new RewardDTO(reward);

        assertEquals(100, dto.getPointsGained());
        assertEquals(50, dto.getBeeCoinsGained());
        assertNotNull(dto.getItemGained());
        assertEquals("Bee Hat", dto.getItemGained().getName());
    }

    @Test
    void constructor_setsFieldsCorrectly_whenItemGainedIsNull() {
        Reward reward = new Reward();
        reward.setPointsGained(200);
        reward.setBeeCoinsGained(75);
        reward.setItemGained(null);

        RewardDTO dto = new RewardDTO(reward);

        assertEquals(200, dto.getPointsGained());
        assertEquals(75, dto.getBeeCoinsGained());
        assertNull(dto.getItemGained());
    }
}
