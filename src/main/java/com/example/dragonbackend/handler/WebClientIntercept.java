package com.example.dragonbackend.handler;


public interface WebClientIntercept {
    enum MethodName {
        GetTasks,
        SolveTask,
        GetShop,
        BuyItem,
        GetReputation,
        StartGame
    }
    void write(MethodName methodName, String req, String resp);
}
