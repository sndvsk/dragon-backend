package com.example.dragonbackend;

import com.example.dragonbackend.service.GameExecutor;
import com.example.dragonbackend.service.GameFSM;
import com.example.dragonbackend.service.GameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class DragonBackendApplicationTests {

    @Autowired
    private GameFSM gameFSM;

    @Autowired
    private GameExecutor gameExecutor;

    @Autowired
    private GameService gameService;

    @Test
    void contextLoads() {
        assertThat(gameFSM).isNotNull();
        assertThat(gameExecutor).isNotNull();
        assertThat(gameService).isNotNull();
    }

}
