package com.beewise.controller.dto;

import com.beewise.model.ItemCategory;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NewShopItemDTO {
    @NotBlank(message = "Name cannot be empty")
    private String name;

    @Enumerated(EnumType.STRING)
    private ItemCategory category;

    @NotBlank(message = "ImageURL cannot be empty")
    private String image;

    @Min(value = 0, message = "price cannot be negative")
    private int price;
}