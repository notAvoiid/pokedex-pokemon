package com.crud.pokemon.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customAPI() {
        final String LINKED_IN = "https://www.linkedin.com/in/igoranascimento/";
        return new OpenAPI()
                .info(new Info()
                        .title("Pokemon CRUD")
                        .version("v1.0")
                        .description("This API is a CRUD that simulates a pokedex!")
                        .summary("This API is a CRUD That simulates a pokedex!")
                        .termsOfService(LINKED_IN)
                        .contact(new Contact().name("Igor").url(LINKED_IN).email("igorabreu.dev@gmail.com"))
                        .license(new License().name("Apache 2.0").url(LINKED_IN)))
                .externalDocs(new ExternalDocumentation()
                        .description("Go to Login page!")
                        .url("/login"));
    }
}
