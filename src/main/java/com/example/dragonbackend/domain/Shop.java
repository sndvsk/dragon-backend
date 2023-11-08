package com.example.dragonbackend.domain;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shop {

    private boolean shoppingSuccess;
    private int gold;
    private int lives;
    private int level;
    private int turn;

}
