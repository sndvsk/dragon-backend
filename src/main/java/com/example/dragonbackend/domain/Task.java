package com.example.dragonbackend.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    @JsonProperty("adId")
    private String taskId;
    private String message;
    private int reward;
    private int expiresIn;
    private Integer encrypted;
    private String probability;

}
