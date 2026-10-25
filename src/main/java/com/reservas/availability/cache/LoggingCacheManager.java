package com.reservas.availability.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;

/** Envuelve cada caché para que las operaciones de Spring y las programáticas queden en el log. */
public class LoggingCacheManager implements CacheManager {

	private final CacheManager delegate;

	public LoggingCacheManager(CacheManager delegate) {
		this.delegate = delegate;
	}

	@Override
	public Cache getCache(String name) {
		Cache cache = delegate.getCache(name);
		return cache == null ? null : new LoggingCache(cache);
	}

	@Override
	public Collection<String> getCacheNames() {
		return delegate.getCacheNames();
	}
}
