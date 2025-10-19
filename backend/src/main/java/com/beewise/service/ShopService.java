package com.beewise.service;

import com.beewise.controller.dto.NewShopItemDTO;
import com.beewise.model.ShopItem;

public interface ShopService {
    ShopItem getItemByName(String name);
    ShopItem createShopItem(NewShopItemDTO dto);
    ShopItem getItem(Long id);
}
