package com.coupon.challenge.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AuthService {

    private final WebClient webClient;

    @Value("${mercadolibre.client-id}")
    private String clientId;

    @Value("${mercadolibre.client-secret}")
    private String clientSecret;

    @Value("${mercadolibre.redirect-uri}")
    private String redirectUri; 

    public AuthService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<String> getAccessToken(String authorizationCode) {
        // Verificar las variables antes de enviar la solicitud
        return webClient.post()
            .uri("/oauth/token")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                    .with("client_id", clientId)
                    .with("client_secret", clientSecret)
                    .with("code", authorizationCode)
                    .with("redirect_uri", redirectUri + "/callback"))
            .retrieve()
            .bodyToMono(String.class);
    }
}