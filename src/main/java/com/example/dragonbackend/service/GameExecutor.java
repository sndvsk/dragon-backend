package com.example.dragonbackend.service;

import com.example.dragonbackend.domain.GameResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class GameExecutor {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final GameService gameService;

    private final ExecutorService executor;

    public GameExecutor(GameService gameService) {
        this.gameService = gameService;

        var counter = new AtomicLong();
        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        this.executor = Executors.newFixedThreadPool(numberOfThreads,
                r -> new Thread(r, String.format("game_worker-%d", counter.getAndIncrement())));
    }

    public void runGames(int numberOfGames) {

        Instant startTime = Instant.now();

        AtomicInteger wins = new AtomicInteger(0);
        AtomicInteger losses = new AtomicInteger(0);
        DoubleSummaryStatistics scoreStatistics = new DoubleSummaryStatistics();
        List<String> successGameIds = new ArrayList<>();
        List<String> failedGameIds = new ArrayList<>();

        // List to hold all the CompletableFuture<GameResult> objects
        List<CompletableFuture<GameResult>> gameFutures = new ArrayList<>();

        // Submit tasks to the executor using CompletableFuture
        for (int i = 0; i < numberOfGames; i++) {
            CompletableFuture<GameResult> future = CompletableFuture.supplyAsync(() -> {
                        GameFSM gameInstance = new GameFSM(gameService);
                        return gameInstance.play(numberOfGames);
                    }, executor)
                    // Handle exceptions for individual futures
                    .whenComplete((game, ex) -> {
                        if (ex != null) {
                            logger.error("Game failed with an exception: {}", ex.getMessage());
                            failedGameIds.add(game.getGameId());
                        }
                    });
            gameFutures.add(future);
        }

        // Wait for all futures to complete
        CompletableFuture<Void> allGames = CompletableFuture.allOf(gameFutures.toArray(new CompletableFuture[0]));

        try {
            logger.info("{}", executor);
            logger.info("Waiting games to complete.");
            allGames.get(); // Block and wait for all games to complete
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Error while waiting games", e);
        }

        logger.info("{}", executor);

        gameFutures.stream()
                .filter(f -> !f.isCompletedExceptionally())
                .map(this::unwrapFuture)
                .forEach(result -> {
                    if (result != null && result.isGameResult()) {
                        wins.incrementAndGet();
                    } else {
                        losses.incrementAndGet();
                    }
                    if (result != null) {
                        scoreStatistics.accept(result.getFinalScore());
                        successGameIds.add(result.getGameId());
                    }
                });

        // Shutdown the executor
        executor.shutdown();

        // Timing and results calculation after all games have finished
        Instant endTime = Instant.now();
        Duration totalDuration = Duration.between(startTime, endTime);
        long averageTimePerGameMillis = totalDuration.toMillis() / numberOfGames;

        printResults(wins.get(), losses.get(), numberOfGames, scoreStatistics.getAverage(),
                averageTimePerGameMillis, totalDuration,
                successGameIds, failedGameIds);
    }

    private GameResult unwrapFuture(CompletableFuture<GameResult> f) {
        try {
            return f.get();
        } catch (InterruptedException | ExecutionException e) { // This should not happen
            logger.error("Thread error: {}", e.getMessage());
        }
        return null;
    }

    private void printResults(int wins, int losses, int totalGames, double meanScore,
                              long averageTimePerGameMillis, Duration totalDuration,
                              List<String> successGameIds, List<String> failedGameIds) {
        int failedGamesSize = failedGameIds.size();
        if (totalGames == 1) {
            // If there is only one game, print a simple message
            String resultMessage = wins > 0 ? "You won!" : "You lost.";
            logger.info("{} Your score: {}", resultMessage, (int) meanScore);
            logger.info("Time taken: {} ms", totalDuration.toMillis());
        } else {
            // If there are multiple games, print detailed statistics
            double winPercentage = (wins / (double) totalGames) * 100;
            String winPercentageFormatted = String.format("%.2f", winPercentage);
            String meanScoreFormatted = String.format("%.2f", meanScore);
            logger.info("Games played: {}, Wins: {}, Losses: {}, Failed: {}, Win Percentage: {}%, Mean Score: {}",
                    totalGames, wins, losses, failedGamesSize, winPercentageFormatted, meanScoreFormatted);
            logger.info("Total time taken: {} ms, Average time per game: {} ms",
                    totalDuration.toMillis(), averageTimePerGameMillis);
            logger.info("Game IDs: {}", successGameIds.toString());
            if (failedGamesSize > 0) {
                logger.info("Failed game IDs: {}", failedGameIds);
            }
        }
    }

}
