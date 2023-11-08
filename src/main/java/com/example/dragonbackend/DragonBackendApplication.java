package com.example.dragonbackend;


import com.example.dragonbackend.service.GameExecutor;
import com.example.dragonbackend.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;

@SpringBootApplication
public class DragonBackendApplication implements CommandLineRunner {

    @Autowired
    private ApplicationContext appContext;

    @Autowired
    private Environment environment;

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final GameService gameService;

    public DragonBackendApplication(GameService gameService) {
        this.gameService = gameService;
    }

    public static void main(String[] args) {
        // Check for verbose argument
        boolean verbose = Arrays.asList(args).contains("--verbose");
        if (verbose) {
            // Only enable debug logging for your package
            System.setProperty("logging.level.com.example.dragonbackend", "DEBUG");
        }

        SpringApplication app = new SpringApplication(DragonBackendApplication.class);
        app.run(args);
    }


    @Override
    public void run(String... args) {
        logger.info("Application is live.");

        if (Arrays.asList(environment.getActiveProfiles()).contains("test")) {
            // Skip running the games if in test profile
            logger.info("Skipping game execution in test environment.");
            return;
        }

        // Parse the number of games to play from command-line arguments
        int numberOfGames = 1; // Default to 1 game
        for (String arg : args) {
            if (arg.matches("\\d+")) { // Check if the argument is a positive integer
                numberOfGames = Integer.parseInt(arg);
                break; // Assuming only one numeric argument is expected for the number of games
            }
        }

        // Initialize the GameExecutorConfig with the instance of GameService
        GameExecutor gameExecutor = new GameExecutor(gameService);

        // Run the games
        gameExecutor.runGames(numberOfGames);

        if (numberOfGames == 1) {
            logger.info("Game ended. Thank you for playing!");
        } else {
            logger.info("All games have ended. Thank you for playing!");
        }

        ((ConfigurableApplicationContext) appContext).close();
    }

}
