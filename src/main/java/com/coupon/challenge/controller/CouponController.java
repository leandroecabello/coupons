package com.coupon.challenge.controller;

import com.coupon.challenge.service.CouponService;
import com.coupon.challenge.dto.CouponRequest;
import com.coupon.challenge.exception.InvalidRequestException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;

import reactor.core.publisher.Mono;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;


@RestController
@RequestMapping("/v1/coupon")
@Tag(name = "Coupon API", description = "Endpoints para gestionar cupones e ítems favoritos")
public class CouponController {
    private static final Logger logger = LoggerFactory.getLogger(CouponController.class);

    @Autowired
    private CouponService couponService;

    @GetMapping("/")
    public String status() {
        return "Application is running!";
    }

    @PostMapping("/")
    @Operation(
        summary = "Calcula los ítems óptimos para un cupón",
        description = "Devuelve los ítems que se pueden comprar sin exceder el monto del cupón.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Ejemplo de solicitud",
                    value = "{\n" +
                            "  \"item_ids\": [\"MLA1\", \"MLA2\", \"MLA3\", \"MLA4\", \"MLA5\"],\n" +
                            "  \"amount\": 500\n" +
                            "}"
                )
            )
        )
    )
    public Mono<ResponseEntity<Map<String, Object>>> getOptimalItems(@RequestBody CouponRequest request) {
        if (request.getItemIds() == null || request.getItemIds().isEmpty()) {
            logger.error("La lista de item_ids no puede estar vacía.");
            return Mono.just(ResponseEntity.badRequest().body(Map.of("error", "La lista de item_ids no puede estar vacía.")));
        }
        
        if (request.getAmount() <= 0) {
            logger.error("El monto debe ser mayor a 0.");
            return Mono.just(ResponseEntity.badRequest().body(Map.of("error", "El monto debe ser mayor a 0.")));
        }
        
        return couponService.calculateOptimalItems(request.getItemIds(), request.getAmount())
            .map(ResponseEntity::ok);
    }

    @GetMapping("/stats")
    @Operation(summary = "Obtiene el Top 5 de ítems favoritos", description = "Devuelve el Top 5 de ítems favoritos.")
    public ResponseEntity<List<Map<String, Object>>> getTopFiveFavorites() {
        // Llama al servicio para obtener el Top 5
        List<Map<String, Object>> topFavorites = couponService.getTopFavorites();
        return ResponseEntity.ok(topFavorites);
    }
}