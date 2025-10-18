package com.beewise.controller;

import com.beewise.controller.dto.ShopItemDTO;
import com.beewise.model.ShopItem;
import com.beewise.service.ShopItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shopItem")
public class ShopItemController {
    private final ShopItemService service;

    public ShopItemController(ShopItemService service) {
        this.service = service;
    }

    @PostMapping()
    public ResponseEntity<ShopItemDTO> createItem(ShopItemDTO dto) {
        ShopItem item = service.createShopItem(dto);
        return ResponseEntity.ok(new ShopItemDTO(item));
    }
}
