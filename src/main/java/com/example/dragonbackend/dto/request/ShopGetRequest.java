package com.example.dragonbackend.dto.request;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShopGetRequest implements Request {

    public String gameId;
}
