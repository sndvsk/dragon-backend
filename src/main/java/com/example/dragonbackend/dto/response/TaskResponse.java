package com.example.dragonbackend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponse {

    @JsonProperty("adId")
    private String taskId;

    @JsonProperty("message")
    private String message;

    @JsonProperty("reward")
    private int reward;

    @JsonProperty("expiresIn")
    private int expiresIn;

    @JsonProperty("probability")
    private String probability;

    @JsonProperty("encrypted")
    private Integer encrypted;

}
