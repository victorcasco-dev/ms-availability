package com.reservas.availability.dto;

import com.reservas.commons.dto.PageMetadataDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Profesores activos paginados (solo lectura).
 */
@Getter
@Setter
public class ProfessorReferencePageDto {

	private List<ProfessorReferenceDto> content;
	private PageMetadataDto page;
}
