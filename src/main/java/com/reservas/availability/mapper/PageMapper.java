package com.reservas.availability.mapper;

import com.reservas.commons.dto.PageMetadataDto;
import com.reservas.commons.dto.TimeSlotDto;
import com.reservas.commons.dto.TimeSlotPageDto;
import com.reservas.commons.entity.TimeSlot;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PageMapper {

	private final TimeSlotMapper timeSlotMapper;

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
}
