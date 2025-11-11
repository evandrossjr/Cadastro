package com.essjr.Cadastro.config;


import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.PublicKey;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi apiGroup() {
        return GroupedOpenApi.builder()
                .group("API")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public GroupedOpenApi webGroup() {
        return GroupedOpenApi.builder()
                .group("Web (Thymeleaf)")
                .pathsToMatch("/**")
                .packagesToExclude("/api/**")
                .build();
    }
}
