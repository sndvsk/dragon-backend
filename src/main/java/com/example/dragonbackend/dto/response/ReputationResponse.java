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
public class ReputationResponse {

    @JsonProperty("people")
    private float people;

    @JsonProperty("state")
    private float state;

    @JsonProperty("underworld")
    private float underworld;
}
