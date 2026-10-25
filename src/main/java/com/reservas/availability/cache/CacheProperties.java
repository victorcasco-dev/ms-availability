package com.reservas.availability.cache;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

/** Dominios de caché y su retención. Todo sale de {@code app.cache.*}. */
@Getter
@Setter
@ConfigurationProperties(prefix = "app.cache")
public class CacheProperties {

	private String keyPrefix;
	private Duration defaultTtl;
	private Map<String, Domain> domains = new LinkedHashMap<>();

	@Getter
	@Setter
	public static class Domain {
		private Duration ttl;
		private boolean persistent;
	}
}
