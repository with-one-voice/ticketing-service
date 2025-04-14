package com.onevoice.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    // application.yml에서 주입받는 gateway 기준 경로
    @Value("${swagger.server-url}")
    private String serverUrl;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("OneVoice API")
                .description("OneVoice 서비스의 OpenAPI 문서입니다.")
                .version("v1"))
            .servers(List.of(new Server().url(serverUrl)))
            .components(new Components()
                .addSecuritySchemes("bearerAuth", new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")))
            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
