package com.reservas.availability.mapper;

import com.reservas.commons.entity.Professor;
import com.reservas.availability.dto.ProfessorReferenceDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapeo Entity → DTO de referencia de profesor.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProfessorReferenceMapper {

	ProfessorReferenceDto toDto(Professor professor);
}
