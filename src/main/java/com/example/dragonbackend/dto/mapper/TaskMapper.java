package com.example.dragonbackend.dto.mapper;

import com.example.dragonbackend.domain.Task;
import com.example.dragonbackend.dto.response.TaskResponse;

public class TaskMapper {

    public static TaskResponse toDTO(Task task) {
        if (task == null) return null;

        TaskResponse response = new TaskResponse();
        response.setTaskId(task.getTaskId());
        response.setMessage(task.getMessage());
        response.setReward(task.getReward());
        response.setExpiresIn(task.getExpiresIn());
        response.setProbability(task.getProbability());
        response.setEncrypted(task.getEncrypted());
        return response;
    }

    public static Task toEntity(TaskResponse response) {
        if (response == null) return null;

        Task task = new Task();
        task.setTaskId(response.getTaskId());
        task.setMessage(response.getMessage());
        task.setReward(response.getReward());
        task.setExpiresIn(response.getExpiresIn());
        task.setProbability(response.getProbability());
        task.setEncrypted(response.getEncrypted());
        return task;
    }
}

