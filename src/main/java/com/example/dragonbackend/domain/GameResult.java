package com.example.dragonbackend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameResult {

    private boolean gameResult;
    private int finalScore;
    private String gameId;
}
