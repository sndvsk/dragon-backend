package com.example.dragonbackend.handler;

public class NullWebClientIntercept implements WebClientIntercept {

    @Override
    public void write(MethodName methodName, String req, String resp) {
    }
}
