package com.reservas.availability.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/** Metadatos OpenAPI de ms-availability. */
@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI msAvailabilityOpenApi() {
		return new OpenAPI()
				.info(new Info()
						.title("ms-availability")
						.version("1.0.0")
						.description("Professor time slots. Search supports query string (gateway) or JSON body."));
	}

	@Bean
	public OpenApiCustomizer gatewayServerUrl(@Value("${app.openapi.server-url}") String gatewayUrl) {
		return openApi -> openApi.servers(List.of(
				new Server().url(gatewayUrl).description("API Gateway")));
	}
}
