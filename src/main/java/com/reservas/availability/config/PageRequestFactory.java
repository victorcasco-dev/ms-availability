package com.reservas.availability.config;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PageRequestFactory {

	private final PaginationProperties paginationProperties;

	public PageRequest from(Integer page, Integer size, Sort sort) {
		int resolvedPage = page == null ? 0 : Math.max(page, 0);
		int resolvedSize = size == null ? paginationProperties.getDefaultSize() : size;
		if (resolvedSize < 1) {
			resolvedSize = paginationProperties.getDefaultSize();
		}
		if (resolvedSize > paginationProperties.getMaxSize()) {
			resolvedSize = paginationProperties.getMaxSize();
		}
		return PageRequest.of(resolvedPage, resolvedSize, sort);
	}
}
