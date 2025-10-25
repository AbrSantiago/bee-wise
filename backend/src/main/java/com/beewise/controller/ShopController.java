package com.beewise.controller;

import com.beewise.controller.dto.NewShopItemDTO;
import com.beewise.controller.dto.ShopItemDTO;
import com.beewise.controller.dto.UserDTO;
import com.beewise.model.ItemCategory;
import com.beewise.model.ShopItem;
import com.beewise.model.User;
import com.beewise.service.ShopService;
import com.beewise.service.UserService;
import com.beewise.service.impl.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/shop")
public class ShopController {
    private final ShopService service;
    private final UserService userService;
    private final JwtService jwtService;

    public ShopController(ShopService service, UserService userService, JwtService jwtService) {
        this.service = service;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping()
    public ResponseEntity<ShopItemDTO> createItem(@RequestBody NewShopItemDTO dto) {
        ShopItem item = service.createShopItem(dto);
        return ResponseEntity.ok(new ShopItemDTO(item));
    }

    @GetMapping
    public ResponseEntity<List<ShopItemDTO>> getAllItems() {
        List<ShopItem> items = service.getAll();
        return ResponseEntity.ok(items.stream().map(ShopItemDTO::new).toList());
    }

    @GetMapping("/allByCategory")
    public ResponseEntity<Map<ItemCategory, List<ShopItemDTO>>> getItemsByCategory() {
        Map<ItemCategory, List<ShopItem>> groupedItems = service.getAllByCategory();

        Map<ItemCategory, List<ShopItemDTO>> groupedDTOs = groupedItems.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream().map(ShopItemDTO::new).toList()
                ));

        return ResponseEntity.ok(groupedDTOs);
    }

    @GetMapping("/free")
    public ResponseEntity<List<ShopItemDTO>> getFreeItems() {
        List<ShopItem> items = service.getFreeItems();
        return ResponseEntity.ok(items.stream().map(ShopItemDTO::new).toList());
    }

    @PutMapping("/buy/{itemId}")
    public ResponseEntity<UserDTO> buyItem(
            @PathVariable Long itemId,
            @RequestHeader("Authorization") String userToken
    ) {
        String username = jwtService.extractUsername(userToken.substring(7));
        User item = userService.buyItem(itemId, username);
        return ResponseEntity.ok(new UserDTO(item));
    }
}
