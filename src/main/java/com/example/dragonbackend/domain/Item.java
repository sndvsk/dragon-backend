package com.example.dragonbackend.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @JsonProperty("id")
    private String itemId;
    private String name;
    private int cost;

}
