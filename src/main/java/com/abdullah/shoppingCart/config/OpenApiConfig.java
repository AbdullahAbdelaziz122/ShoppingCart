package com.abdullah.shoppingCart.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Shopping Cart API")
                        .version("1.0")
                        .description("API documentation for the E-commerce Shopping Cart backend project.")
                        .contact(new Contact()
                                .name("Abdullah")
                                .email("support@example.com")));
    }
}
