package com.example.dragonbackend.dto.mapper;

import com.example.dragonbackend.domain.Item;
import com.example.dragonbackend.dto.response.ItemResponse;

public class ItemMapper {

    public static ItemResponse toDTO(Item item) {
        if (item == null) return null;

        ItemResponse response = new ItemResponse();
        response.setItemId(item.getItemId());
        response.setName(item.getName());
        response.setCost(item.getCost());
        return response;
    }

    public static Item toEntity(ItemResponse response) {
        if (response == null) return null;

        Item item = new Item();
        item.setItemId(response.getItemId());
        item.setName(response.getName());
        item.setCost(response.getCost());
        return item;
    }
}
