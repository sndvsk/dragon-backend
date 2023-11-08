package com.example.dragonbackend.dto.mapper;

import com.example.dragonbackend.domain.Reputation;
import com.example.dragonbackend.dto.response.ReputationResponse;

public class ReputationMapper {

    public static ReputationResponse toDTO(Reputation reputation) {
        if (reputation == null) return null;

        ReputationResponse response = new ReputationResponse();
        response.setPeople(reputation.getPeople());
        response.setState(reputation.getState());
        response.setUnderworld(reputation.getUnderworld());
        return response;
    }

    public static Reputation toEntity(ReputationResponse response) {
        if (response == null) return null;

        Reputation reputation = new Reputation();
        reputation.setPeople(response.getPeople());
        reputation.setState(response.getState());
        reputation.setUnderworld(response.getUnderworld());
        return reputation;
    }
}
