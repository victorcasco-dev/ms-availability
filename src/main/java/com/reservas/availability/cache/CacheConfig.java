package com.reservas.availability.cache;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@EnableCaching
@EnableConfigurationProperties(CacheProperties.class)
public class CacheConfig implements CachingConfigurer {

	private static final Logger log = LoggerFactory.getLogger(CacheConfig.class);

	private final CacheProperties properties;
	private final RedisConnectionFactory connectionFactory;

	public CacheConfig(CacheProperties properties, RedisConnectionFactory connectionFactory) {
		this.properties = properties;
		this.connectionFactory = connectionFactory;
	}

	@Bean
	@Override
	public org.springframework.cache.CacheManager cacheManager() {
		if (!StringUtils.hasText(properties.getKeyPrefix())) {
			throw new IllegalStateException("app.cache.key-prefix is required");
		}
		if (properties.getDefaultTtl() == null || properties.getDefaultTtl().isZero() || properties.getDefaultTtl().isNegative()) {
			throw new IllegalStateException("app.cache.default-ttl must be a positive duration");
		}
		GenericJackson2JsonRedisSerializer values = new GenericJackson2JsonRedisSerializer()
				.configure(mapper -> {
					mapper.registerModule(new JavaTimeModule());
					mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
				});
		RedisCacheConfiguration defaults = configuration(values, properties.getDefaultTtl(), false);
		Map<String, RedisCacheConfiguration> domains = new LinkedHashMap<>();
		properties.getDomains().forEach((name, domain) -> {
			boolean persistent = domain.isPersistent();
			Duration ttl = domain.getTtl() == null ? properties.getDefaultTtl() : domain.getTtl();
			if (!persistent && (ttl == null || ttl.isZero() || ttl.isNegative())) {
				throw new IllegalStateException("app.cache.domains." + name + ".ttl must be a positive duration");
			}
			domains.put(name, configuration(values, ttl, persistent));
			if (persistent) {
				log.info("Cache domain {} retention=persistent", name);
			} else {
				log.info("Cache domain {} retention=ttl {}", name, ttl);
			}
		});
		RedisCacheManager redis = RedisCacheManager.builder(connectionFactory)
				.cacheDefaults(defaults)
				.withInitialCacheConfigurations(domains)
				.transactionAware()
				.build();
		return new LoggingCacheManager(redis);
	}

	@Bean
	@Override
	public CacheErrorHandler errorHandler() {
		return new LoggingCacheErrorHandler();
	}

	private RedisCacheConfiguration configuration(
			GenericJackson2JsonRedisSerializer values,
			Duration ttl,
			boolean persistent
	) {
		RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
				.disableCachingNullValues()
				.computePrefixWith(cacheName -> properties.getKeyPrefix() + ":" + cacheName + ":")
				.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
				.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(values));
		if (!persistent) {
			configuration = configuration.entryTtl(ttl);
		}
		return configuration;
	}
}
