package com.example.dragonbackend.dto.mapper;

import com.example.dragonbackend.domain.Game;
import com.example.dragonbackend.dto.response.GameResponse;

public class GameMapper {

    public static GameResponse toDTO(Game game) {
        if (game == null) return null;

        GameResponse response = new GameResponse();
        response.setGameId(game.getGameId());
        response.setLives(game.getLives());
        response.setGold(game.getGold());
        response.setLevel(game.getLevel());
        response.setScore(game.getScore());
        response.setHighScore(game.getHighScore());
        response.setTurn(game.getTurn());
        return response;
    }

    public static Game toEntity(GameResponse response) {
        if (response == null) return null;

        Game game = new Game();
        game.setGameId(response.getGameId());
        game.setLives(response.getLives());
        game.setGold(response.getGold());
        game.setLevel(response.getLevel());
        game.setScore(response.getScore());
        game.setHighScore(response.getHighScore());
        game.setTurn(response.getTurn());
        return game;
    }
}
