package com.beewise.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "shop_items")
@NoArgsConstructor
public class ShopItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private ItemCategory category;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private int price;

    public ShopItem(String name, ItemCategory category, String image, int price) {
        this.name = name;
        this.category = category;
        this.imageUrl = image;
        this.price = price;
    }
}
