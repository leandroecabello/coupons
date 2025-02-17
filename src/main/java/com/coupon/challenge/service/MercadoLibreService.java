package com.coupon.challenge.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class MercadoLibreService {

    private static final Logger logger = LoggerFactory.getLogger(MercadoLibreService.class);
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
        logger.info("Iniciando consulta de precios para {} productos", itemIds.size());

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
        logger.info("Consultando precio para item ID: {}", itemId);
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

    public Mono<List<String>> fetchProductIds(String category, int limit) {
        String uri = category != null
                ? "/sites/MLA/search?category=" + category
                : "/sites/MLA/search";

                logger.info("Consultando productos con categoría: {} y límite: {}", category, limit);

                return webClient.get()
            .uri(uri)
            .exchangeToMono(response -> {
                logger.info("Código de respuesta: {}", response.statusCode());

                if (response.statusCode().isError()) {
                    return response.bodyToMono(String.class)
                            .doOnNext(body -> logger.error("Respuesta con error: {}", body))
                            .flatMap(body -> Mono.error(new RuntimeException("Error en API: " + body)));
                }

                return response.bodyToMono(String.class)
                        .doOnNext(body -> logger.info("Respuesta cruda de la API de Mercado Libre: {}", body))
                        .flatMap(body -> {
                            try {
                                ObjectMapper objectMapper = new ObjectMapper();
                                Map<String, Object> jsonResponse = objectMapper.readValue(body, new TypeReference<>() {});

                                if (!jsonResponse.containsKey("results")) {
                                    logger.error("La respuesta no contiene 'results'. Respuesta: {}", jsonResponse);
                                    return Mono.error(new RuntimeException("Estructura JSON inesperada"));
                                }

                                List<Map<String, Object>> results = (List<Map<String, Object>>) jsonResponse.get("results");

                                if (results == null || results.isEmpty()) {
                                    logger.warn("No se encontraron resultados para la categoría: {}", category);
                                    return Mono.just(List.of());
                                }

                                List<String> productIds = results.stream()
                                        .map(item -> (String) item.get("id"))
                                        .limit(limit)
                                        .collect(Collectors.toList());

                                logger.info("Se obtuvieron {} productos", productIds.size());
                                return Mono.just(productIds);
                            } catch (Exception e) {
                                logger.error("Error procesando la respuesta JSON de Mercado Libre: {}", body, e);
                                return Mono.error(new RuntimeException("Error procesando JSON"));
                            }
                        });
            });
    }
}
