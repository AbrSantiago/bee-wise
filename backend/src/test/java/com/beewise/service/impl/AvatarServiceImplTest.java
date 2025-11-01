package com.beewise.service.impl;

import com.beewise.controller.dto.AvatarDTO;
import com.beewise.controller.dto.ShopItemDTO;
import com.beewise.exception.AvatarDoesNotExistsException;
import com.beewise.model.Avatar;
import com.beewise.model.ItemCategory;
import com.beewise.model.ShopItem;
import com.beewise.model.User;
import com.beewise.controller.dto.NewShopItemDTO;
import com.beewise.repository.AvatarRepository;
import com.beewise.repository.UserRepository;
import com.beewise.service.AvatarService;
import com.beewise.service.ShopService;
import com.beewise.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class AvatarServiceImplTest {

    @Autowired
    private AvatarService avatarService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private UserService userService;

    @Autowired
    private AvatarRepository avatarRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        createItem("Default skin", ItemCategory.SKIN);
        createItem("Default hair", ItemCategory.HAIR);
        createItem("Default shirt", ItemCategory.SHIRT);
        createItem("Default background", ItemCategory.BACKGROUND);
    }

    private void createItem(String name, ItemCategory category) {
        NewShopItemDTO item = new NewShopItemDTO();
        item.setName(name);
        item.setCategory(category);
        item.setImage("");
        item.setPrice(0);
        shopService.createShopItem(item);
    }

    @Test
    void newDefaultAvatar_setsDefaultItemsAndSavesAvatar() {
        User user = new User();
        user.setUsername("avatarUser");
        user.setEmail("avatarUser@test.com");
        user.setName("Avatar");
        user.setSurname("User");
        user.setPasswordHash("password123");
        user = userRepository.save(user);

        Avatar avatar = avatarService.newDefaultAvatar(user);

        assertNotNull(avatar.getId());
        assertEquals(user.getId(), avatar.getUser().getId());
        assertEquals("Default skin", avatar.getSkin().getName());
        assertEquals("Default hair", avatar.getHair().getName());
        assertEquals("Default shirt", avatar.getShirt().getName());
        assertEquals("Default background", avatar.getBackground().getName());

        Avatar avatarFromDb = avatarRepository.findById(avatar.getId()).orElse(null);
        assertNotNull(avatarFromDb);
        assertEquals(user.getId(), avatarFromDb.getUser().getId());
    }

    @Test
    void updateAvatar_updatesAvatarWithNewItems() {
        User user = new User();
        user.setUsername("avatarUser2");
        user.setEmail("avatarUser2@test.com");
        user.setName("Avatar2");
        user.setSurname("User2");
        user.setPasswordHash("password123");
        user = userRepository.save(user);

        Avatar avatar = avatarService.newDefaultAvatar(user);

        // Crea nuevos ítems para actualizar
        createItem("New skin", ItemCategory.SKIN);
        createItem("New hair", ItemCategory.HAIR);
        createItem("New shirt", ItemCategory.SHIRT);
        createItem("New background", ItemCategory.BACKGROUND);

        ShopItem newSkin = shopService.getItemByName("New skin");
        ShopItem newHair = shopService.getItemByName("New hair");
        ShopItem newShirt = shopService.getItemByName("New shirt");
        ShopItem newBackground = shopService.getItemByName("New background");


        AvatarDTO dto = new AvatarDTO();
        dto.setId(avatar.getId());
        dto.setSkin(new ShopItemDTO(newSkin));
        dto.setHair(new ShopItemDTO(newHair));
        dto.setShirt(new ShopItemDTO(newShirt));
        dto.setBackground(new ShopItemDTO(newBackground));

        Avatar updated = avatarService.updateAvatar(avatar.getId(), dto);

        assertEquals("New skin", updated.getSkin().getName());
        assertEquals("New hair", updated.getHair().getName());
        assertEquals("New shirt", updated.getShirt().getName());
        assertEquals("New background", updated.getBackground().getName());
    }

    @Test
    void updateAvatar_throwsExceptionIfAvatarDoesNotExist() {
        AvatarDTO dto = new AvatarDTO();
        dto.setId(999L);
        dto.setSkin(new ShopItemDTO(shopService.getItemByName("Default skin")));
        dto.setHair(new ShopItemDTO(shopService.getItemByName("Default hair")));
        dto.setShirt(new ShopItemDTO(shopService.getItemByName("Default shirt")));
        dto.setBackground(new ShopItemDTO(shopService.getItemByName("Default background")));

        assertThrows(AvatarDoesNotExistsException.class, () -> {
        avatarService.updateAvatar(999L, dto);
        });
    }
}