package com.coupon.challenge.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Arrays;

@Service
public class MercadoLibreService {

    private final WebClient webClient;

    /**
     * Construye un nuevo MercadoLibreService con el WebClient especificado.
     *
     * @param webClient el WebClient que se utilizará para realizar solicitudes HTTP
     */
    public MercadoLibreService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Map<String, Double>> getPrices(List<String> itemIds) {
        return Mono.zip(
            itemIds.stream()
                .map(this::getPrice)
                .collect(Collectors.toList()),
            results -> Arrays.stream(results)
                .map(result -> (Map.Entry<String, Double>) result)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        );
    }

    private Mono<Map.Entry<String, Double>> getPrice(String itemId) {
        return webClient.get()
            .uri("/items/{itemId}", itemId)
            .retrieve()
            .onStatus(
                status -> status.isError(), // Condición para detectar error HTTP
                response -> response.bodyToMono(String.class)
                    .map(body -> new RuntimeException("Error en la API de Mercado Libre: " + body))
            )
            .bodyToMono(Map.class) // Deserializa la respuesta como un mapa
            .map(item -> Map.entry(itemId, ((Number) item.get("price")).doubleValue())); // Devuelve la entrada del mapa con el id y el precio
    }
}
