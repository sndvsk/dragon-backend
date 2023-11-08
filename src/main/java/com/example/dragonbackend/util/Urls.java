package com.example.dragonbackend.util;

public class Urls {

    public static final String BASE_URL = "https://dragonsofmugloar.com/api/v2/";

    // Endpoint URLs
    public static final String START_GAME = BASE_URL + "game/start";
    public static String getReputationUrl(String gameId) {
        return BASE_URL + gameId + "/investigate/reputation";
    }
    public static String getTasksUrl(String gameId) {
        return BASE_URL + gameId + "/messages";
    }
    public static String getSolveTaskUrl(String gameId, String adId) {
        return BASE_URL + gameId + "/solve/" + adId;
    }
    public static String getShopUrl(String gameId) {
        return BASE_URL + gameId + "/shop";
    }
    public static String getBuyItemUrl(String gameId, String itemId) {
        return BASE_URL + gameId + "/shop/buy/" + itemId;
    }
}
