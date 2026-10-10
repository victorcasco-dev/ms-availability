package com.reservas.availability.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.pagination")
public class PaginationProperties {

	private int defaultSize = 20;
	private int maxSize = 100;
}
