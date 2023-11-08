package com.example.dragonbackend;

import com.example.dragonbackend.dto.request.MessageGetRequest;
import com.example.dragonbackend.dto.request.MessagePostRequest;
import com.example.dragonbackend.dto.request.ShopGetRequest;
import com.example.dragonbackend.dto.request.ShopPostRequest;
import com.example.dragonbackend.dto.response.*;
import com.example.dragonbackend.handler.WebClientIntercept;
import com.example.dragonbackend.service.GameService;
import com.example.dragonbackend.util.Urls;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.example.dragonbackend.util.MockData.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClientIntercept intercept;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private GameService gameService;

    @BeforeEach
    void setUp() {
        lenient().when(webClient.get()).thenReturn(requestHeadersUriSpec);
        lenient().when(webClient.post()).thenReturn(requestBodyUriSpec);
        lenient().when(requestBodyUriSpec.retrieve()).thenReturn(responseSpec);
        lenient().when(requestHeadersUriSpec.uri(any(String.class))).thenReturn(requestHeadersSpec);
        lenient().when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void startGame_ShouldReturnGameResponse() {
        GameResponse mockResponse = new GameResponse();
        mockResponse.setGameId(mockGameId);

        lenient().when(requestBodyUriSpec.uri(Urls.START_GAME)).thenReturn(requestBodyUriSpec);

        given(responseSpec.bodyToMono(GameResponse.class)).willReturn(Mono.just(mockResponse));

        GameResponse result = gameService.startGame();

        assertThat(result).isNotNull();
        assertThat(result.getGameId()).isEqualTo(mockGameId);
    }

    @Test
    void getTasks_ShouldReturnTaskResponseArray() {
        TaskResponse[] mockResponse = mockTaskResponses;

        lenient().when(requestBodyUriSpec.uri(Urls.getTasksUrl(mockGameId))).thenReturn(requestBodyUriSpec);

        given(responseSpec.bodyToMono(TaskResponse[].class)).willReturn(Mono.just(mockResponse));

        TaskResponse[] result = gameService.getTasks(new MessageGetRequest(mockGameId));

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(mockResponse);
    }

    @Test
    void solveTask_ShouldReturnMessageResponse() {
        MessageResponse mockResponse = mockMessageResponse;
        String taskId = mockTaskResponses[3].getTaskId();

        lenient().when(requestBodyUriSpec.uri(Urls.getSolveTaskUrl(mockGameId, taskId))).thenReturn(requestBodyUriSpec);

        given(responseSpec.bodyToMono(MessageResponse.class)).willReturn(Mono.just(mockResponse));

        MessageResponse result = gameService.solveTask(new MessagePostRequest(mockGameId, taskId));

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(mockResponse);
    }

    @Test
    void getShop_ShouldReturnItemResponseArray() {
        ItemResponse[] mockResponse = mockShopItems;


        lenient().when(requestBodyUriSpec.uri(Urls.getShopUrl(mockGameId))).thenReturn(requestBodyUriSpec);

        given(responseSpec.bodyToMono(ItemResponse[].class)).willReturn(Mono.just(mockResponse));

        ItemResponse[] result = gameService.getShop(new ShopGetRequest(mockGameId));

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(mockResponse);
    }

    @Test
    void buyItem_ShouldReturnShopResponse() {
        ShopResponse mockResponse = mockShopResponse;

        String itemId = mockShopItems[2].getItemId();

        lenient().when(requestBodyUriSpec.uri(Urls.getBuyItemUrl(mockGameId, itemId))).thenReturn(requestBodyUriSpec);

        given(responseSpec.bodyToMono(ShopResponse.class)).willReturn(Mono.just(mockResponse));

        ShopResponse result = gameService.buyItem(new ShopPostRequest(mockGameId, itemId));

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(mockResponse);
    }
}
