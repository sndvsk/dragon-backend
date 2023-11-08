package com.example.dragonbackend.dto.mapper;

import com.example.dragonbackend.domain.Shop;
import com.example.dragonbackend.dto.response.ShopResponse;

public class ShopMapper {

    public static ShopResponse toDTO(Shop shop) {
        if (shop == null) return null;

        ShopResponse response = new ShopResponse();
        response.setShoppingSuccess(shop.isShoppingSuccess());
        response.setGold(shop.getGold());
        response.setLives(shop.getLives());
        response.setLevel(shop.getLevel());
        response.setTurn(shop.getTurn());
        return response;
    }

    public static Shop toEntity(ShopResponse response) {
        if (response == null) return null;

        Shop shop = new Shop();
        shop.setShoppingSuccess(response.isShoppingSuccess());
        shop.setGold(response.getGold());
        shop.setLives(response.getLives());
        shop.setLevel(response.getLevel());
        shop.setTurn(response.getTurn());
        return shop;
    }
}
