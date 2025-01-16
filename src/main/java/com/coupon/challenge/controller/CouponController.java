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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/coupon")
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
    @Operation(summary = "Calcula los ítems óptimos para un cupón", description = "Devuelve los ítems que se pueden comprar sin exceder el monto del cupón.")
    public ResponseEntity<Map<String, Object>> getOptimalItems(@RequestBody CouponRequest request) {
        // Validar que la lista de items y el monto no sean nulos o vacíos
        if (request.getItemIds() == null || request.getItemIds().isEmpty()) {
            logger.error("La lista de item_ids no puede estar vacía.");
            throw new InvalidRequestException("La lista de item_ids no puede estar vacía.");
        }

        if (request.getAmount() <= 0) {
            logger.error("El monto debe ser mayor a 0.");
            throw new InvalidRequestException("El monto debe ser mayor a 0.");
        }


        Map<String, Object> response = couponService.calculateOptimalItems(request.getItemIds(), request.getAmount());
        return ResponseEntity.ok(response);
    }
}