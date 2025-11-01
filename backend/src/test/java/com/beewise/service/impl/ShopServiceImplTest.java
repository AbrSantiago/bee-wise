package com.beewise.service.impl;

import com.beewise.controller.dto.NewShopItemDTO;
import com.beewise.exception.ShopItemDoesNotExistsException;
import com.beewise.model.ItemCategory;
import com.beewise.model.ShopItem;
import com.beewise.repository.ShopItemRepository;
import com.beewise.service.ShopService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ShopServiceImplTest {

    @Autowired
    private ShopService shopService;

    @Autowired
    private ShopItemRepository shopItemRepository;

    private ShopItem item1;
    private ShopItem item2;
    private ShopItem item3;

    @BeforeEach
    void setUp() {
        item1 = shopService.createShopItem(newItemDTO("Item1", ItemCategory.SKIN, "img1", 0));
        item2 = shopService.createShopItem(newItemDTO("Item2", ItemCategory.HAIR, "img2", 10));
        item3 = shopService.createShopItem(newItemDTO("Item3", ItemCategory.HAIR, "img3", 0));
    }

    private NewShopItemDTO newItemDTO(String name, ItemCategory category, String image, int price) {
        NewShopItemDTO dto = new NewShopItemDTO();
        dto.setName(name);
        dto.setCategory(category);
        dto.setImage(image);
        dto.setPrice(price);
        return dto;
    }

    @Test
    void getItemByName_returnsCorrectItem() {
        ShopItem found = shopService.getItemByName("Item1");
        assertNotNull(found);
        assertEquals(item1.getId(), found.getId());
    }

    @Test
    void getItemByName_throwsExceptionIfNotFound() {
        assertThrows(ShopItemDoesNotExistsException.class, () -> {
            shopService.getItemByName("NoSuchItem");
        });
    }

    @Test
    void createShopItem_createsAndReturnsItem() {
        NewShopItemDTO dto = newItemDTO("NewItem", ItemCategory.SHIRT, "imgX", 5);
        ShopItem created = shopService.createShopItem(dto);
        assertNotNull(created.getId());
        assertEquals("NewItem", created.getName());
        assertEquals(ItemCategory.SHIRT, created.getCategory());
        assertEquals("imgX", created.getImageUrl());
        assertEquals(5, created.getPrice());
    }

    @Test
    void getItem_returnsCorrectItem() {
        ShopItem found = shopService.getItem(item2.getId());
        assertNotNull(found);
        assertEquals(item2.getName(), found.getName());
    }

    @Test
    void getItem_throwsExceptionIfNotFound() {
        assertThrows(ShopItemDoesNotExistsException.class, () -> {
            shopService.getItem(99999L);
        });
    }

    @Test
    void getAll_returnsAllItems() {
        List<ShopItem> items = shopService.getAll();
        assertTrue(items.size() >= 3);
        assertTrue(items.stream().anyMatch(i -> i.getName().equals("Item1")));
        assertTrue(items.stream().anyMatch(i -> i.getName().equals("Item2")));
        assertTrue(items.stream().anyMatch(i -> i.getName().equals("Item3")));
    }

    @Test
    void getAllByCategory_returnsGroupedItems() {
        Map<ItemCategory, List<ShopItem>> grouped = shopService.getAllByCategory();
        assertTrue(grouped.containsKey(ItemCategory.SKIN));
        assertTrue(grouped.containsKey(ItemCategory.HAIR));
        assertEquals(1, grouped.get(ItemCategory.SKIN).size());
        assertEquals(2, grouped.get(ItemCategory.HAIR).size());
    }

    @Test
    void getFreeItems_returnsOnlyFreeItems() {
        List<ShopItem> freeItems = shopService.getFreeItems();
        assertTrue(freeItems.stream().allMatch(i -> i.getPrice() == 0));
        assertTrue(freeItems.stream().anyMatch(i -> i.getName().equals("Item1")));
        assertTrue(freeItems.stream().anyMatch(i -> i.getName().equals("Item3")));
    }
}