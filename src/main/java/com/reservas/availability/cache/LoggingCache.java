package com.reservas.availability.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

import java.util.concurrent.Callable;

/** Delega en Redis y registra get, put, evict y clear. No registra el valor. */
public class LoggingCache implements Cache {

	private static final Logger log = LoggerFactory.getLogger(LoggingCache.class);

	private final Cache delegate;

	public LoggingCache(Cache delegate) {
		this.delegate = delegate;
	}

	@Override
	public String getName() {
		return delegate.getName();
	}

	@Override
	public Object getNativeCache() {
		return delegate.getNativeCache();
	}

	@Override
	public ValueWrapper get(Object key) {
		ValueWrapper value = delegate.get(key);
		log.info("Cache GET {} cache={} key={}", value == null ? "miss" : "hit", getName(), key);
		return value;
	}

	@Override
	public <T> T get(Object key, Class<T> type) {
		T value = delegate.get(key, type);
		log.info("Cache GET {} cache={} key={}", value == null ? "miss" : "hit", getName(), key);
		return value;
	}

	@Override
	public <T> T get(Object key, Callable<T> valueLoader) {
		log.info("Cache GET load cache={} key={}", getName(), key);
		return delegate.get(key, valueLoader);
	}

	@Override
	public void put(Object key, Object value) {
		log.info("Cache PUT cache={} key={}", getName(), key);
		delegate.put(key, value);
	}

	@Override
	public ValueWrapper putIfAbsent(Object key, Object value) {
		log.info("Cache PUT_IF_ABSENT cache={} key={}", getName(), key);
		return delegate.putIfAbsent(key, value);
	}

	@Override
	public void evict(Object key) {
		log.info("Cache EVICT cache={} key={}", getName(), key);
		delegate.evict(key);
	}

	@Override
	public boolean evictIfPresent(Object key) {
		log.info("Cache EVICT cache={} key={}", getName(), key);
		return delegate.evictIfPresent(key);
	}

	@Override
	public void clear() {
		log.info("Cache CLEAR cache={}", getName());
		delegate.clear();
	}

	@Override
	public boolean invalidate() {
		log.info("Cache CLEAR cache={}", getName());
		return delegate.invalidate();
	}
}
