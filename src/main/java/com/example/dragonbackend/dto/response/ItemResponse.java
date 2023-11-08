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
public class ItemResponse {

    @JsonProperty("id")
    private String itemId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("cost")
    private int cost;
}
