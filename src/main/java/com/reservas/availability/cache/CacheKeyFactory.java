package com.reservas.availability.cache;

import org.springframework.stereotype.Component;

/** Clave {@code tipo:id}. El prefijo y el dominio los agrega el CacheManager desde la configuración. */
@Component("cacheKeys")
public class CacheKeyFactory {

	public String of(String type, Object id) {
		return type + ":" + id;
	}
}
