package com.coupon.challenge.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Coupon Challenge API")
                        .version("1.0")
                        .description("API para maximizar el uso de un cupón y gestionar ítems favoritos."));
    }
}
