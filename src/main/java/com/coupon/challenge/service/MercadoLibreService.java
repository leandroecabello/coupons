package com.coupon.challenge.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        String ids = String.join(",", itemIds);

        // Llamada a la API de Mercado Libre con manejo de errores
        return webClient.get()
        .uri(uriBuilder -> uriBuilder.path("/items").queryParam("ids", ids).build())
        .retrieve()
        .onStatus(
            status -> status.isError(), // Condición para detectar error HTTP
            response -> response.bodyToMono(String.class)
                .map(body -> new RuntimeException("Error en la API de Mercado Libre: " + body))
        )
        .bodyToMono(List.class) // Deserializa la respuesta como una lista
        .map(list -> ((List<Map<String, Object>>) list).stream()
                .collect(Collectors.toMap(
                        item -> (String) item.get("id"),
                        item -> ((Number) item.get("price")).doubleValue()
                ))
        ); // Devuelve el flujo con el mapa procesado
    }
}
