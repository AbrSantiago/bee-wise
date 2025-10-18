package com.beewise.service.impl;

import com.beewise.controller.dto.AvatarDTO;
import com.beewise.exception.AvatarDoesNotExistsException;
import com.beewise.model.Avatar;
import com.beewise.model.ShopItem;
import com.beewise.model.User;
import com.beewise.repository.AvatarRepository;
import com.beewise.service.AvatarService;
import com.beewise.service.ShopService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AvatarServiceImpl implements AvatarService {
    private final AvatarRepository repository;
    private final ShopService shopService;

    public AvatarServiceImpl(AvatarRepository repository, ShopService shopService) {
        this.repository = repository;
        this.shopService = shopService;
    }

    @Override
    public Avatar newDefaultAvatar(User user) {
        Avatar avatar = new Avatar();
        avatar.setUser(user);
        avatar.setSkin(shopService.getItemByName("Default skin"));
        avatar.setHair(shopService.getItemByName("Default hair"));
        avatar.setShirt(shopService.getItemByName("Default shirt"));
        avatar.setBackground(shopService.getItemByName("Default background"));
        return repository.save(avatar);
    }

    @Override
    public Avatar updateAvatar(Long id, AvatarDTO dto) {
        Avatar avatar = repository.findById(id)
                .orElseThrow(() -> new AvatarDoesNotExistsException("Avatar with id " + id + " does not exists"));
        ShopItem skin = shopService.getItem(dto.getSkin().getId());
        ShopItem hair = shopService.getItem(dto.getHair().getId());
        ShopItem shirt = shopService.getItem(dto.getShirt().getId());
        ShopItem background = shopService.getItem(dto.getBackground().getId());
        avatar.setSkin(skin);
        avatar.setHair(hair);
        avatar.setShirt(shirt);
        avatar.setBackground(background);
        return repository.save(avatar);
    }
}
