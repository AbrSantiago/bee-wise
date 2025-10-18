package com.beewise.service.impl;

import com.beewise.controller.dto.ShopItemDTO;
import com.beewise.exception.ShopItemDoesNotExistsException;
import com.beewise.model.ShopItem;
import com.beewise.repository.ShopItemRepository;
import com.beewise.service.ShopItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ShopItemServiceImpl implements ShopItemService {
    private final ShopItemRepository repository;

    public ShopItemServiceImpl(ShopItemRepository repository) {
        this.repository = repository;
    }

    @Override
    public ShopItem getItemByName(String name) {
        return repository.findByName(name)
                .orElseThrow(() -> new ShopItemDoesNotExistsException("Item " + name + " does tot exists"));
    }

    @Override
    public ShopItem createShopItem(ShopItemDTO dto) {
        ShopItem item = new ShopItem(
                dto.getName(),
                dto.getCategory(),
                dto.getImage(),
                dto.getPrice()
        );
        return repository.save(item);
    }
}
