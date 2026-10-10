package com.reservas.availability.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
