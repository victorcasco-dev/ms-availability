package com.reservas.availability.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.function.Function;

/**
 * Guarda elementos sueltos (una búsqueda paginada no se cachea completa).
 * Usa el {@link CacheManager} de forma programática.
 */
@Component
public class IndividualCache {

	private static final Logger log = LoggerFactory.getLogger(IndividualCache.class);

	private final CacheManager cacheManager;

	public IndividualCache(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public <T> void putEach(String cacheName, Collection<T> values, Function<T, String> keyOf) {
		if (values == null || values.isEmpty()) {
			return;
		}
		for (T value : values) {
			if (value == null) {
				continue;
			}
			String key = keyOf.apply(value);
			if (key == null || key.endsWith(":null")) {
				continue;
			}
			put(cacheName, key, value);
		}
	}

	public void put(String cacheName, String key, Object value) {
		if (value == null || key == null) {
			return;
		}
		try {
			Cache cache = cacheManager.getCache(cacheName);
			if (cache == null) {
				log.warn("Cache domain not found name={}", cacheName);
				return;
			}
			cache.put(key, value);
		} catch (RuntimeException ex) {
			log.error("Cache PUT failed cache={} key={}", cacheName, key, ex);
		}
	}

	public void evict(String cacheName, String key) {
		try {
			Cache cache = cacheManager.getCache(cacheName);
			if (cache == null) {
				log.warn("Cache domain not found name={}", cacheName);
				return;
			}
			cache.evict(key);
		} catch (RuntimeException ex) {
			log.error("Cache EVICT failed cache={} key={}", cacheName, key, ex);
		}
	}
}
