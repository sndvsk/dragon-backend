package com.example.dragonbackend.dto.request;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessagePostRequest implements Request {

    private String gameId;
    private String taskId;
}
