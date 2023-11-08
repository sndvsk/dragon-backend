package com.example.dragonbackend.dto.mapper;

import com.example.dragonbackend.domain.Message;
import com.example.dragonbackend.dto.response.MessageResponse;

public class MessageMapper {

    public static MessageResponse toDTO(Message message) {
        if (message == null) return null;

        MessageResponse response = new MessageResponse();
        response.setSuccess(message.isSuccess());
        response.setLives(message.getLives());
        response.setGold(message.getGold());
        response.setScore(message.getScore());
        response.setHighScore(message.getHighScore());
        response.setTurn(message.getTurn());
        response.setMessage(message.getMessage());
        return response;
    }

    public static Message toEntity(MessageResponse response) {
        if (response == null) return null;

        Message message = new Message();
        message.setSuccess(response.isSuccess());
        message.setLives(response.getLives());
        message.setGold(response.getGold());
        message.setScore(response.getScore());
        message.setHighScore(response.getHighScore());
        message.setTurn(response.getTurn());
        message.setMessage(response.getMessage());
        return message;
    }
}
