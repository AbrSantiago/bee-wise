package com.beewise.service;

import com.beewise.controller.dto.NewShopItemDTO;
import com.beewise.model.ItemCategory;
import com.beewise.model.ShopItem;

import java.util.List;
import java.util.Map;

public interface ShopService {
    ShopItem getItemByName(String name);
    ShopItem createShopItem(NewShopItemDTO dto);
    ShopItem getItem(Long id);
    List<ShopItem> getAll();
    Map<ItemCategory, List<ShopItem>> getAllByCategory();
}
