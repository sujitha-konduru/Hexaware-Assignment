package com.example.demo.config;


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
    public OpenAPI insuranceOpenAPI() {
        return new OpenAPI()
            .info(new Info().title("Insurance Management API")
                .description("Backend API for managing vehicle insurance proposals and policies")
                .version("v1.0.0")
                .contact(new Contact()
                    .name("Support Team")
                    .email("support@insuranceapp.com")
                    .url("https://insuranceapp.com"))
                .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0.html"))
            )
            .externalDocs(new ExternalDocumentation()
                .description("GitHub Repo")
                .url("https://github.com/your-repo"));
    }
}
