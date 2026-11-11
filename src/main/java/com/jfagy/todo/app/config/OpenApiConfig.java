package com.jfagy.todo.app.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	OpenAPI todoOpenApi() {
		return new OpenAPI().info(new Info()
				.title("Todo API")
				.version("v1")
				.description("CRUD API for persons and their todos."));
	}
}
