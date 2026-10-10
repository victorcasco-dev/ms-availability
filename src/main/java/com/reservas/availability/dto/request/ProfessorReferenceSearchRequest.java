package com.reservas.availability.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

/**
 * Búsqueda paginada de profesores (referencia para crear franjas).
 */
@Getter
@Setter
public class ProfessorReferenceSearchRequest {

	@Min(0)
	private Integer page;

	@Min(1)
	private Integer size;

	private String name;
}
