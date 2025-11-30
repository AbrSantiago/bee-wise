package com.beewise.controller;

import com.beewise.controller.dto.NewShopItemDTO;
import com.beewise.controller.dto.ShopItemDTO;
import com.beewise.controller.dto.UserDTO;
import com.beewise.model.*;
import com.beewise.service.ShopService;
import com.beewise.service.UserService;
import com.beewise.service.impl.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShopControllerTest {

    @Mock
    private ShopService shopService;
    @Mock
    private UserService userService;
    @Mock
    private JwtService jwtService;

    private ShopController controller;

    @BeforeEach
    void setUp() {
        controller = new ShopController(shopService, userService, jwtService);
    }

    @Test
    void createItem_returnsShopItemDTO() {
        NewShopItemDTO dto = new NewShopItemDTO();
        ShopItem item = new ShopItem();
        when(shopService.createShopItem(dto)).thenReturn(item);

        ResponseEntity<ShopItemDTO> response = controller.createItem(dto);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(shopService).createShopItem(dto);
    }

    @Test
    void getAllItems_returnsListOfShopItemDTO() {
        ShopItem item1 = new ShopItem();
        ShopItem item2 = new ShopItem();
        when(shopService.getAll()).thenReturn(List.of(item1, item2));

        ResponseEntity<List<ShopItemDTO>> response = controller.getAllItems();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(shopService).getAll();
    }

    @Test
    void getItemsByCategory_returnsGroupedDTOs() {
        ShopItem item1 = new ShopItem();
        ShopItem item2 = new ShopItem();
        Map<ItemCategory, List<ShopItem>> grouped = Map.of(
                ItemCategory.BACKGROUND, List.of(item1),
                ItemCategory.HAIR, List.of(item2)
        );
        when(shopService.getAllByCategory()).thenReturn(grouped);

        ResponseEntity<Map<ItemCategory, List<ShopItemDTO>>> response = controller.getItemsByCategory();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey(ItemCategory.BACKGROUND));
        assertTrue(response.getBody().containsKey(ItemCategory.HAIR));
        verify(shopService).getAllByCategory();
    }

    @Test
    void getFreeItems_returnsListOfShopItemDTO() {
        ShopItem item1 = new ShopItem();
        ShopItem item2 = new ShopItem();
        when(shopService.getFreeItems()).thenReturn(List.of(item1, item2));

        ResponseEntity<List<ShopItemDTO>> response = controller.getFreeItems();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(shopService).getFreeItems();
    }

    @Test
    void buyItem_returnsUserDTO() {
        Long itemId = 1L;
        String token = "Bearer testtoken";
        String username = "user1";
        User user = getUser();

        when(jwtService.extractUsername("testtoken")).thenReturn(username);
        when(userService.buyItem(itemId, username)).thenReturn(user);

        ResponseEntity<UserDTO> response = controller.buyItem(itemId, token);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(jwtService).extractUsername("testtoken");
        verify(userService).buyItem(itemId, username);
    }

    private static User getUser() {
        User user = new User();
        user.setLevel(getLevel());

        Avatar avatar = new Avatar();
        avatar.setId(123L);
        avatar.setUser(user);

        // Inicializar los ShopItem para evitar NullPointerException
        ShopItem background = new ShopItem();
        background.setId(1L);
        ShopItem shirt = new ShopItem();
        shirt.setId(2L);
        ShopItem skin = new ShopItem();
        skin.setId(3L);
        ShopItem hair = new ShopItem();
        hair.setId(4L);

        avatar.setBackground(background);
        avatar.setShirt(shirt);
        avatar.setSkin(skin);
        avatar.setHair(hair);

        user.setAvatar(avatar);
        return user;
    }

    private static Level getLevel() {
        Level lvl = new Level();
        lvl.setId(1L);
        return lvl;
    }
}