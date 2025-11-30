package com.beewise.repository;

import com.beewise.model.ShopItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShopItemRepository extends JpaRepository<ShopItem, Long> {
    Optional<ShopItem> findByName(String name);

    @Query("SELECT s FROM ShopItem s WHERE s.price = 0")
    List<ShopItem> findAllFreeItems();
}
