package com.beewise.controller.dto;

import com.beewise.model.ItemCategory;
import com.beewise.model.ShopItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ShopItemDTO {
    private Long id;
    private String name;
    private ItemCategory category;
    private String image;
    private int price;

    public ShopItemDTO(ShopItem item) {
        this.id = item.getId();
        this.name = item.getName();
        this.category = item.getCategory();
        this.image = item.getImageUrl();
        this.price = item.getPrice();
    }
}
