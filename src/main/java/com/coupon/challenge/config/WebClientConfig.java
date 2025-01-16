package com.coupon.challenge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class WebClientConfig {

    @Value("${mercadolibre.uri}")
    private String meliUri;

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.baseUrl(meliUri).build();
    }
}