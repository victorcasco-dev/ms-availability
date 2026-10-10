package com.reservas.availability.mapper;

import com.reservas.commons.dto.PageMetadataDto;
import com.reservas.commons.dto.TimeSlotDto;
import com.reservas.commons.dto.TimeSlotPageDto;
import com.reservas.commons.entity.Professor;
import com.reservas.commons.entity.TimeSlot;
import com.reservas.availability.dto.ProfessorReferencePageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

/**
 * Conversión de páginas Spring Data a DTOs paginados del API.
 */
@Component
@RequiredArgsConstructor
public class PageMapper {

	private final TimeSlotMapper timeSlotMapper;
	private final ProfessorReferenceMapper professorReferenceMapper;

	public PageMetadataDto toMetadata(Page<?> page) {
		PageMetadataDto metadata = new PageMetadataDto();
		metadata.setPage(page.getNumber());
		metadata.setSize(page.getSize());
		metadata.setTotalElements(page.getTotalElements());
		metadata.setTotalPages(page.getTotalPages());
		return metadata;
	}

	public TimeSlotPageDto toTimeSlotPage(Page<TimeSlot> page) {
		TimeSlotPageDto dto = new TimeSlotPageDto();
		dto.setContent(page.getContent().stream().map(timeSlotMapper::toDto).toList());
		dto.setPage(toMetadata(page));
		return dto;
	}

	public ProfessorReferencePageDto toProfessorReferencePage(Page<Professor> page) {
		ProfessorReferencePageDto dto = new ProfessorReferencePageDto();
		dto.setContent(page.getContent().stream().map(professorReferenceMapper::toDto).toList());
		dto.setPage(toMetadata(page));
		return dto;
	}
}
