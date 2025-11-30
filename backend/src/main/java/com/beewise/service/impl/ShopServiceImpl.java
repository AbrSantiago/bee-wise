package com.beewise.service.impl;

import com.beewise.controller.dto.NewShopItemDTO;
import com.beewise.exception.ShopItemDoesNotExistsException;
import com.beewise.model.ItemCategory;
import com.beewise.model.ShopItem;
import com.beewise.repository.ShopItemRepository;
import com.beewise.service.ShopService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ShopServiceImpl implements ShopService {
    private final ShopItemRepository repository;

    public ShopServiceImpl(ShopItemRepository repository) {
        this.repository = repository;
    }

    @Override
    public ShopItem getItemByName(String name) {
        return repository.findByName(name)
                .orElseThrow(() -> new ShopItemDoesNotExistsException("Item " + name + " does tot exists"));
    }

    @Override
    public ShopItem createShopItem(NewShopItemDTO dto) {
        ShopItem item = new ShopItem(
                dto.getName(),
                dto.getCategory(),
                dto.getImage(),
                dto.getPrice()
        );
        return repository.save(item);
    }

    @Override
    public ShopItem getItem(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ShopItemDoesNotExistsException("Item with " + id + " does not exists"));
    }

    @Override
    public List<ShopItem> getAll() {
        return repository.findAll();
    }

    public Map<ItemCategory, List<ShopItem>> getAllByCategory() {
        List<ShopItem> items = repository.findAll();

        return items.stream()
                .collect(Collectors.groupingBy(ShopItem::getCategory));
    }

    @Override
    public List<ShopItem> getFreeItems() {
        return repository.findAllFreeItems();
    }
}
