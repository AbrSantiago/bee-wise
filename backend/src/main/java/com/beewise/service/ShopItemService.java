package com.beewise.service;

import com.beewise.controller.dto.ShopItemDTO;
import com.beewise.model.ShopItem;

public interface ShopItemService {
    ShopItem getItemByName(String name);
    ShopItem createShopItem(ShopItemDTO dto);
}
