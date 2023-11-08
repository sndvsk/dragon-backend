package com.example.dragonbackend.service;

import com.example.dragonbackend.domain.GameResult;
import com.example.dragonbackend.domain.Item;
import com.example.dragonbackend.domain.enums.ActionType;
import com.example.dragonbackend.domain.enums.Difficulty;
import com.example.dragonbackend.domain.enums.GameState;
import com.example.dragonbackend.dto.mapper.ItemMapper;
import com.example.dragonbackend.dto.request.MessageGetRequest;
import com.example.dragonbackend.dto.request.MessagePostRequest;
import com.example.dragonbackend.dto.request.ShopGetRequest;
import com.example.dragonbackend.dto.request.ShopPostRequest;
import com.example.dragonbackend.dto.response.GameResponse;
import com.example.dragonbackend.dto.response.ItemResponse;
import com.example.dragonbackend.dto.response.MessageResponse;
import com.example.dragonbackend.dto.response.ShopResponse;
import com.example.dragonbackend.dto.response.TaskResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Game finite state machine.
 */
@Service

public class GameFSM {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final GameService gameService;
    private GameState currentState;

    private String currentGameId;
    //private ReputationResponse reputation;
    private TaskResponse[] tasks;
    private ItemResponse[] items;

    private final List<Item> ownedItems = new ArrayList<>();
    private TaskResponse chosenTask;
    private ItemResponse chosenItem;
    private int currentGold;
    private int currentLives;
    private boolean gameResult;
    private int finalScore;
    private int retryNoTasks;

    public GameFSM(GameService gameService) {
        this.gameService = gameService;
        this.currentState = GameState.START_GAME;
    }

    public GameResult play(int numberOfGames) {
        logger.debug("game {} started", this.hashCode());
        while (currentState != GameState.END) {
            currentState = nextState(numberOfGames);
        }
        logger.debug("game {} completed", this.hashCode());
        return getGameResult();
    }

    private GameState nextState(int numberOfGames) {
        return switch (currentState) {
            case START_GAME -> handleStartGame(numberOfGames);
            case GET_TASKS -> handleGetTasks();
            case EVALUATE_TASKS -> handleEvaluateTasks();
            case SOLVE_TASK -> handleSolveTask();
            //case GET_REPUTATION -> handleCheckReputation();
            //case EVALUATE_REPUTATION -> handleEvaluateReputation();
            case GET_SHOP_ITEMS -> handleGetShopItems();
            case BUY_POTION -> handleBuyPotion();
            case EVALUATE_SHOP_ITEMS -> handleEvaluateShopItems();
            case BUY_SHOP_ITEMS -> handleBuyShopItems();
            case EVALUATE_END_GAME -> handleEvaluateEndGame();
            case END -> currentState;
        };
    }

    private GameState handleStartGame(int numberOfGames) {
        GameResponse game = gameService.startGame();
        currentGameId = game.getGameId();
        if (numberOfGames == 1) logger.info("Game id: {}", currentGameId);
        return currentState = GameState.GET_TASKS;
    }

    private GameState handleGetTasks() {
        if (retryNoTasks < 5) {
            tasks = gameService.getTasks(new MessageGetRequest(currentGameId));
            //logger.debug("Tasks: {}", Arrays.toString(tasks));
            return currentState = GameState.EVALUATE_TASKS;
        } else {
            return currentState = GameState.EVALUATE_END_GAME;
        }
    }

    private GameState handleEvaluateTasks() {
        List<TaskResponse> availableTasks = Arrays.stream(tasks)
                .filter(task -> task.getEncrypted() == null) // filter out encrypted tasks
                .sorted((t1, t2) -> {
                    // Compare difficulties using rank
                    int difficultyComparison = Integer.compare(
                            Difficulty.fromProbability(t1.getProbability()).getRank(),
                            Difficulty.fromProbability(t2.getProbability()).getRank()
                    );
                    if (difficultyComparison != 0) return difficultyComparison;

                    // Compare action priorities
                    int actionComparison = Integer.compare(
                            ActionType.fromMessage(t1.getMessage()).getPriority(),
                            ActionType.fromMessage(t2.getMessage()).getPriority()
                    );
                    if (actionComparison != 0) return actionComparison;

                    // Compare rewards if difficulties are equal
                    return Integer.compare(t2.getReward(), t1.getReward()); // compare rewards in descending order
                })
                .toList();

        if (availableTasks.isEmpty()) {
            chosenTask = null;
            logger.warn("No suitable tasks available.");
            retryNoTasks++;
            return currentState = GameState.GET_SHOP_ITEMS;
        } else {
            chosenTask = availableTasks.get(0); // take the best task
            logger.debug("Chosen taskId: {}", chosenTask.getTaskId());
        }
        return currentState = GameState.SOLVE_TASK;
    }

    private GameState handleSolveTask() {
        logger.debug("Solving task: {}", chosenTask);
        MessageResponse message = gameService.solveTask(new MessagePostRequest(currentGameId, chosenTask.getTaskId()));
        if (message == null) {
            // failed to get response from game server, fail game
            return currentState = GameState.EVALUATE_END_GAME;
        }

        // Update current status
        currentLives = message.getLives();
        currentGold = message.getGold();
        finalScore = message.getScore(); // Always update the score

        // Check for game over conditions
        if (currentLives < 1 || finalScore >= 1000) {
            return currentState = GameState.EVALUATE_END_GAME;
        } else {
            // Handle the result of the task
            if (message.isSuccess()) {
                logger.debug("Task success.");
            } else {
                logger.debug("Task failed. Current lives: {}. Current gold: {}", currentLives, currentGold);
            }

            // buy potions at all times when you can, alternative strategy
            // currentState = (currentGold > 50) ? GameState.BUY_POTION : GameState.GET_SHOP_ITEMS;
            currentState = GameState.GET_SHOP_ITEMS;
        }

        return currentState;
    }


/*    private GameState handleCheckReputation() {
        reputation = gameService.getReputation(new ReputationPostRequest(currentGameId));
        return currentState = GameState.EVALUATE_REPUTATION;
    }

    private GameState handleEvaluateReputation() {
        if (reputation.getPeople() < 0) {
            // do sth
        }
        if (reputation.getState() < 0) {
            // do sth
        }
        if (reputation.getUnderworld() < 0) {
            // do sth
        }
        return currentState = GameState.GET_SHOP_ITEMS;
    }*/

    private GameState handleGetShopItems() {
        logger.debug("Getting shop items.");
        items = gameService.getShop(new ShopGetRequest(currentGameId));
        return currentState = GameState.EVALUATE_SHOP_ITEMS;
    }

    @SuppressWarnings("SameReturnValue")
    private GameState handleBuyPotion() {
        // Check if we have enough gold before attempting to buy
        if (currentGold < 50) {
            logger.debug("Not enough gold to buy health potion. Gold: {}", currentGold);
            return GameState.GET_TASKS; // Directly return the next state
        }

        logger.debug("Attempting to buy health potion.");
        ShopResponse boughtPotion = gameService.buyItem(new ShopPostRequest(currentGameId, "hpot"));

        if (boughtPotion == null) {
            logger.debug("Health potion buying failed due to an unknown error.");
            return GameState.GET_TASKS; // Directly return the next state
        }

        // Update game state with the results from the shopping attempt
        currentGold = boughtPotion.getGold();
        currentLives = boughtPotion.getLives();

        if (boughtPotion.isShoppingSuccess()) {
            logger.debug("Successfully bought health potion. Lives: {}. Gold: {}", currentLives, currentGold);
        } else {
            logger.debug("Failed to buy health potion. Lives: {}. Gold: {}", currentLives, currentGold);
        }

        return GameState.GET_TASKS;
    }


    private GameState handleEvaluateShopItems() {
        logger.debug("Evaluating items from shop.");
        Set<String> ownedItemNames = ownedItems.stream().map(Item::getName).collect(Collectors.toSet());
        chosenItem = Arrays.stream(items)
                .sorted(Comparator.comparingInt(ItemResponse::getCost))
                .filter(item -> !ownedItemNames.contains(item.getName()))
                .filter(item -> !item.getName().equals("Healing potion")) // don't include potion
                .findFirst()
                .orElse(null);
        return currentState = GameState.BUY_SHOP_ITEMS;
    }

    private GameState handleBuyShopItems() {
        logger.debug("Attempting to buy an item from the shop.");

        // Decide whether to reserve gold for a healing potion based on life count
        final int goldToReserveForHealing = (currentLives <= 2) ? 50 : 0;

        // If health is critically low, prioritize buying a potion over other items
        if (currentLives < 2) {
            logger.debug("Health is critically low, prioritizing potion purchase.");
            return GameState.BUY_POTION;
        }

        // Early exit if no item is chosen or if insufficient gold to buy the chosen item and reserve for healing
        if (chosenItem == null || currentGold < (chosenItem.getCost() + goldToReserveForHealing)) {
            logger.debug(
                    "Cannot buy the chosen item due to insufficient gold or no item selected. Gold: {}, Item Cost: {}",
                    currentGold, (chosenItem != null) ? chosenItem.getCost() : "N/A");
            return GameState.GET_TASKS;
        }

        // Proceed to buy the chosen item
        logger.debug("Attempting to buy item: {}", chosenItem);
        ShopResponse boughtItem = gameService.buyItem(new ShopPostRequest(currentGameId, chosenItem.getItemId()));

        if (boughtItem.isShoppingSuccess()) {
            logger.debug("Item purchase successful. Item: {}", chosenItem);
            ownedItems.add(ItemMapper.toEntity(chosenItem));
            currentGold = boughtItem.getGold();
        } else {
            logger.debug("Item purchase failed. Item: {}", chosenItem);
        }

        return GameState.GET_TASKS;
    }

    private GameState handleEvaluateEndGame() {
        logger.debug("Game over. Score: {}. Lives: {}", finalScore, currentLives);
        gameResult = finalScore >= 1000;
        return currentState = GameState.END;
    }

    private GameResult getGameResult() {
        return new GameResult(gameResult, finalScore, currentGameId);
    }

}
