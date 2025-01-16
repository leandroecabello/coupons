package com.coupon.challenge.controller;
import com.coupon.challenge.service.AuthService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/callback")
public class CallbackController {

    private final AuthService authService;

    public CallbackController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public Mono<ResponseEntity<String>> handleCallback(@RequestParam String code) {
        // Llama al servicio y devuelve el Mono directamente
        System.out.println("code: " + code);
        return authService.getAccessToken(code)
                .map(token -> ResponseEntity.ok("Access Token: " + token));
    }
}