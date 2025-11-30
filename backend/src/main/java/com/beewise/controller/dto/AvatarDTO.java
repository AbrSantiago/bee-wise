package com.beewise.controller.dto;

import com.beewise.model.Avatar;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AvatarDTO {
    private Long id;
    private Long userId;
    private ShopItemDTO skin;
    private ShopItemDTO hair;
    private ShopItemDTO shirt;
    private ShopItemDTO background;

    public AvatarDTO(Avatar avatar) {
        this.id = avatar.getId();
        this.userId = avatar.getUser().getId();
        this.skin = new ShopItemDTO(avatar.getSkin());
        this.shirt = new ShopItemDTO(avatar.getShirt());
        this.hair = new ShopItemDTO(avatar.getHair());
        this.background = new ShopItemDTO(avatar.getBackground());
    }
}
