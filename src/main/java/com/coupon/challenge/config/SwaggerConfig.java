package com.coupon.challenge.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.*;


@Configuration
public class SwaggerConfig {

    @Value("${mercadolibre.uri}")
    private String meliUri;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Coupon Challenge API")
                        .version("1.0")
                        .description("API para maximizar el uso de un cupón y gestionar ítems favoritos."))
                .components(new Components()
                        .addSecuritySchemes("oauth2", new SecurityScheme()
                                .type(SecurityScheme.Type.OAUTH2)
                                .flows(new OAuthFlows()
                                        .authorizationCode(new OAuthFlow()
                                                .authorizationUrl(meliUri + "/authorization")
                                                .tokenUrl(meliUri + "/oauth/token")
                                                .scopes(new Scopes()
                                                        .addString("read", "Leer datos privados")
                                                        .addString("write", "Escribir datos privados")
                                                )))))
                .addSecurityItem(new SecurityRequirement().addList("oauth2"));
    }
}
