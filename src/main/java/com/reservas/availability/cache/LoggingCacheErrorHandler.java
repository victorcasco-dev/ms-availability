package com.reservas.availability.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;

/** Un fallo de Redis queda en el log y no se convierte en traza HTTP. La operación de negocio sigue. */
public class LoggingCacheErrorHandler implements CacheErrorHandler {

	private static final Logger log = LoggerFactory.getLogger(LoggingCacheErrorHandler.class);

	@Override
	public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
		log.error("Cache GET failed cache={} key={}", cache.getName(), key, exception);
	}

	@Override
	public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
		log.error("Cache PUT failed cache={} key={}", cache.getName(), key, exception);
	}

	@Override
	public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
		log.error("Cache EVICT failed cache={} key={}", cache.getName(), key, exception);
	}

	@Override
	public void handleCacheClearError(RuntimeException exception, Cache cache) {
		log.error("Cache CLEAR failed cache={}", cache.getName(), exception);
	}
}
