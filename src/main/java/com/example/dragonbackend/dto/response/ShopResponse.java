package com.example.dragonbackend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShopResponse {

    @JsonProperty("shoppingSuccess")
    private boolean shoppingSuccess;

    @JsonProperty("gold")
    private int gold;

    @JsonProperty("lives")
    private int lives;

    @JsonProperty("level")
    private int level;

    @JsonProperty("turn")
    private int turn;

}
