package com.brightsmile.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // Información general de la API
                .info(new Info()
                        .title("BrightSmile Dental API")
                        .version("1.0")
                        .description("REST API for BrightSmile Dental appointment scheduling system")
                        .contact(new Contact()
                                .name("Eder Acuña")
                                .email("ederj08@gmail.com")
                        )
                )
                // Configura JWT en Swagger para poder probar endpoints protegidos
                // Agrega el botón "Authorize" en la UI de Swagger
                .addSecurityItem(new SecurityRequirement()
                        .addList("Bearer Authentication")
                )
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter your JWT token here")
                        )
                );
    }
}