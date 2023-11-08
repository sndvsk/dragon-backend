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
public class GameResponse {

    @JsonProperty("gameId")
    private String gameId;

    @JsonProperty("lives")
    private int lives;

    @JsonProperty("gold")
    private int gold;

    @JsonProperty("level")
    private int level;

    @JsonProperty("score")
    private int score;

    @JsonProperty("highScore")
    private int highScore;

    @JsonProperty("turn")
    private int turn;
}
