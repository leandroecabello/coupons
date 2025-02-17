package com.coupon.challenge.controller;

import com.coupon.challenge.service.MercadoLibreService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/v1/mercadolibre")
@Tag(name = "Mercado Libre", description = "Endpoints relacionados con Mercado Libre")
public class MercadoLibreController {

    private static final Logger logger = LoggerFactory.getLogger(MercadoLibreController.class);
    private final MercadoLibreService mercadoLibreService;

    public MercadoLibreController(MercadoLibreService mercadoLibreService) {
        this.mercadoLibreService = mercadoLibreService;
    }

    @GetMapping("/products/ids")
    @Operation(summary = "Obtiene los IDs de productos", description = "Devuelve una lista de IDs de productos.")
    public Mono<ResponseEntity<List<String>>> getProductIdsByCategory(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "10") int limit
    ) {
        logger.info("Petición recibida: Obtener {} productos de la categoría {}", limit, category);

        return mercadoLibreService.fetchProductIds(category, limit)
                .map(ResponseEntity::ok)
                .doOnError(error -> logger.error("Error en el endpoint /products: {}", error.getMessage()));
    }
}
