package com.reservas.availability.config;

import com.reservas.commons.pagination.PaginationSupport;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
/** Límites de paginación ({@code app.pagination.*}). */
@ConfigurationProperties(prefix = "app.pagination")
public class PaginationProperties implements PaginationSupport {

	private int defaultSize = 20;
	private int maxSize = 100;
}
