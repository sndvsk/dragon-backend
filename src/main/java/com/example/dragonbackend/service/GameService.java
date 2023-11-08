package com.example.dragonbackend.service;

import com.example.dragonbackend.dto.request.MessageGetRequest;
import com.example.dragonbackend.dto.request.MessagePostRequest;
import com.example.dragonbackend.dto.request.ShopGetRequest;
import com.example.dragonbackend.dto.request.ShopPostRequest;
//import com.example.dragonbackend.dto.request.ReputationPostRequest;
import com.example.dragonbackend.dto.response.GameResponse;
import com.example.dragonbackend.dto.response.ItemResponse;
import com.example.dragonbackend.dto.response.MessageResponse;
import com.example.dragonbackend.dto.response.ShopResponse;
import com.example.dragonbackend.dto.response.TaskResponse;
//import com.example.dragonbackend.dto.response.ReputationResponse;
import com.example.dragonbackend.handler.WebClientIntercept;
import com.example.dragonbackend.util.Urls;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.util.function.Supplier;

@Service
public class GameService {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int MAX_RETRIES = 5;
    private static final Duration REQUEST_BLOCK_TIMEOUT = Duration.ofSeconds(5);

    private final WebClient webClient;

    private final WebClientIntercept interceptor;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public GameService(WebClient webClient, WebClientIntercept interceptor) {
        this.webClient = webClient;
        this.interceptor = interceptor;
    }

    private <T> T executeWebRequestWithRetries(Supplier<T> webRequestSupplier, String requestDescription) {
        for (int attempts = 0; attempts < MAX_RETRIES; attempts++) {
            try {
                T response = webRequestSupplier.get();
                if (attempts > 0) {
                    logger.info("Success after retrying {} for attempt {}", requestDescription, attempts);
                }
                return response;
            } catch (WebClientResponseException e) {
                logger.error("Response error on {}: {}", requestDescription, e.getMessage(), e);
            } catch (Exception e) {
                logger.error("Communication error on {}: {}", requestDescription, e.getMessage(), e);
            }
            // Wait 1 second before the next retry.
            try {
                // exponential retry
                Thread.sleep(attempts * 1000);
            } catch (InterruptedException ie) {
                logger.error("Interrupted while waiting to retry {}: {}", requestDescription, ie.getMessage(), ie);
                break; // Exit the retry loop if the thread is interrupted.
            }
        }
        logger.error("Failed all retries for {}", requestDescription);
        return null;
    }

    private void serializeRequestResponse(WebClientIntercept.MethodName methodName, Object req, Object resp) {
        try {
            interceptor.write(methodName,
                    req != null ? objectMapper.writeValueAsString(req) : null,
                    resp != null ? objectMapper.writeValueAsString(resp) : null);
        } catch (JsonProcessingException ignored) {
        }
    }

    public GameResponse startGame() {
        var response = executeWebRequestWithRetries(
                () -> webClient.post()
                        .uri(Urls.START_GAME)
                        .retrieve()
                        .bodyToMono(GameResponse.class)
                        .block(REQUEST_BLOCK_TIMEOUT),
                "start game");
        serializeRequestResponse(WebClientIntercept.MethodName.StartGame, null, response);
        return response;
    }

    /*public ReputationResponse getReputation(ReputationPostRequest request) {
        var response = executeWebRequestWithRetries(
                () -> webClient.post()
                        .uri(Urls.getReputationUrl(request.getGameId()))
                        .retrieve()
                        .bodyToMono(ReputationResponse.class)
                        .block(REQUEST_BLOCK_TIMEOUT),
                "get reputation");
        serializeRequestResponse(WebClientIntercept.MethodName.GetReputation, request, response);
        return response;
    }*/

    public TaskResponse[] getTasks(MessageGetRequest request) {
        var response = executeWebRequestWithRetries(
                () -> webClient.get()
                        .uri(Urls.getTasksUrl(request.getGameId()))
                        .retrieve()
                        .bodyToMono(TaskResponse[].class)
                        .block(REQUEST_BLOCK_TIMEOUT),
                "get tasks");
        serializeRequestResponse(WebClientIntercept.MethodName.GetTasks, request, response);
        return response;
    }

    public MessageResponse solveTask(MessagePostRequest request) {
        var response = executeWebRequestWithRetries(
                () -> webClient.post()
                        .uri(Urls.getSolveTaskUrl(request.getGameId(), request.getTaskId()))
                        .retrieve()
                        .bodyToMono(MessageResponse.class)
                        .block(REQUEST_BLOCK_TIMEOUT),
                "solve task");
        serializeRequestResponse(WebClientIntercept.MethodName.SolveTask, request, response);
        return response;
    }

    public ItemResponse[] getShop(ShopGetRequest request) {
        var response = executeWebRequestWithRetries(
                () -> webClient.get()
                        .uri(Urls.getShopUrl(request.getGameId()))
                        .retrieve()
                        .bodyToMono(ItemResponse[].class)
                        .block(REQUEST_BLOCK_TIMEOUT),
                "get shop");
        serializeRequestResponse(WebClientIntercept.MethodName.GetShop, request, response);
        return response;
    }

    public ShopResponse buyItem(ShopPostRequest request) {
        var response = executeWebRequestWithRetries(
                () -> webClient.post()
                        .uri(Urls.getBuyItemUrl(request.getGameId(), request.getItemId()))
                        .retrieve()
                        .bodyToMono(ShopResponse.class)
                        .block(REQUEST_BLOCK_TIMEOUT),
                "buy item");
        serializeRequestResponse(WebClientIntercept.MethodName.BuyItem, request, response);
        return response;
    }
}
