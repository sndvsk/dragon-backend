package com.example.dragonbackend;

import com.example.dragonbackend.dto.request.MessageGetRequest;
import com.example.dragonbackend.dto.request.MessagePostRequest;
import com.example.dragonbackend.dto.request.ShopGetRequest;
import com.example.dragonbackend.dto.request.ShopPostRequest;
import com.example.dragonbackend.dto.response.*;
import com.example.dragonbackend.handler.WebClientIntercept;
import com.example.dragonbackend.service.GameFSM;
import com.example.dragonbackend.service.GameService;
import com.example.dragonbackend.util.Tuple;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.invocation.InterceptedInvocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ActiveProfiles;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.invoke.MethodHandles;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameFSMTest {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Mock
    private GameService gameService;

    @InjectMocks
    private GameFSM gameFSM;

    private Map<Integer, Tuple> prepareData(JSONArray source, String gameId) throws JsonProcessingException {
        var objectMapper = new ObjectMapper();
        var dataMap = new HashMap<Integer, Tuple>();

        for (int seq = 0; seq < source.size(); seq++) {
            var event = (JSONObject)source.get(seq);
            var eventType = WebClientIntercept.MethodName.valueOf(event.get("event").toString());

            switch (eventType) {
                case StartGame -> {
                    //var request = (JSONObject) event.get("request");
                    var response = (JSONObject) event.get("response");
                    dataMap.put(seq, Tuple.of(
                            null,
                            objectMapper.readValue(JSONObject.toJSONString(response), GameResponse.class),
                            (reqExp, argsActual) -> true));
                }
                case GetTasks -> {
                    var request = (JSONObject) event.get("request");
                    var response = (JSONArray) event.get("response");
                    dataMap.put(seq, Tuple.of(
                            objectMapper.readValue(JSONObject.toJSONString(request), MessageGetRequest.class),
                            objectMapper.readValue(JSONArray.toJSONString(response), TaskResponse[].class),
                            (reqExp, argsActual) ->
                                    gameId.compareTo(((MessageGetRequest)argsActual[0]).getGameId()) == 0)
                    );
                }
                case SolveTask -> {
                    var request = (JSONObject) event.get("request");
                    var response = (JSONObject) event.get("response");
                    dataMap.put(seq, Tuple.of(
                            objectMapper.readValue(JSONObject.toJSONString(request), MessagePostRequest.class),
                            objectMapper.readValue(JSONObject.toJSONString(response), MessageResponse.class),
                            (reqExp, argsActual) ->
                                    gameId.compareTo(((MessagePostRequest)argsActual[0]).getGameId()) == 0)
                    );
                }
                case GetShop -> {
                    var request = (JSONObject) event.get("request");
                    var response = (JSONArray) event.get("response");
                    dataMap.put(seq, Tuple.of(
                            objectMapper.readValue(JSONObject.toJSONString(request), ShopGetRequest.class),
                            objectMapper.readValue(JSONArray.toJSONString(response), ItemResponse[].class),
                            (reqExp, argsActual) ->
                                    gameId.compareTo(((ShopGetRequest)argsActual[0]).getGameId()) == 0)
                    );
                }
                case BuyItem -> {
                    var request = (JSONObject) event.get("request");
                    var response = (JSONObject) event.get("response");
                    dataMap.put(seq, Tuple.of(
                            objectMapper.readValue(JSONObject.toJSONString(request), ShopPostRequest.class),
                            objectMapper.readValue(JSONObject.toJSONString(response), ShopResponse.class),
                            (reqExp, argsActual) ->
                                    gameId.compareTo(((ShopPostRequest)argsActual[0]).getGameId()) == 0)
                    );
                }
            }
        }
        return dataMap;
    }

    @Test
    void playCompleteGameFromExternalFile() throws JsonProcessingException, FileNotFoundException, ParseException {
        var gameId = "QLSk8wmB";
        var p = Path.of("testData/494a4867-4bce-43d0-a8bc-49f5cf200aa8.json");
        var jp = new JSONParser();
        var testData = (JSONArray) jp.parse(new FileReader(p.toFile()));
        var dataMap = prepareData(testData, gameId);

        int mockOffset = 6;
        var dataSupplier = new Function<InvocationOnMock, Object>() {
            @Override
            public Object apply(InvocationOnMock invocation) {
                var iv = (InterceptedInvocation)invocation;
                logger.info("#{} {}", iv.getSequenceNumber(), invocation.getMethod().getName());
                var data = dataMap.get(iv.getSequenceNumber() - mockOffset);
                assertTrue(data.validate.apply(data.request, invocation.getArguments()));
                return data.response;
            }
        };

        when(gameService.startGame()).then(dataSupplier::apply);
        when(gameService.getTasks(any(MessageGetRequest.class))).then(dataSupplier::apply);
        when(gameService.solveTask(any(MessagePostRequest.class))).then(dataSupplier::apply);
        when(gameService.getShop(any(ShopGetRequest.class))).then(dataSupplier::apply);
        when(gameService.buyItem(any(ShopPostRequest.class))).then(dataSupplier::apply);

        var result = gameFSM.play(1);
        logger.info("{}", result);

        assertTrue(result.isGameResult());
    }
}
