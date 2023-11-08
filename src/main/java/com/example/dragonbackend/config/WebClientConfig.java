package com.example.dragonbackend.config;

import com.example.dragonbackend.handler.FileWebClientIntercept;
import com.example.dragonbackend.handler.NullWebClientIntercept;
import com.example.dragonbackend.handler.WebClientIntercept;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import static com.example.dragonbackend.util.Urls.BASE_URL;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.create(BASE_URL);
    }

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Bean
    public WebClientIntercept webClientIntercept() {
        return activeProfile != null && activeProfile.contains("dev")  ?
                new FileWebClientIntercept() : new NullWebClientIntercept();
    }
}
