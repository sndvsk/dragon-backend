package com.example.dragonbackend.util;

import java.util.function.BiFunction;

public class Tuple {
    public final Object request;
    public final Object response;

    public final BiFunction<Object, Object[], Boolean> validate;

    Tuple(Object request, Object response, BiFunction<Object, Object[], Boolean> validate) {
        this.request = request;
        this.response = response;
        this.validate = validate;
    }

    public static Tuple of(Object req, Object resp, BiFunction<Object, Object[], Boolean> validate) {
        return new Tuple(req, resp, validate);
    }
}
