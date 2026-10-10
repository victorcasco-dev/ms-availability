package com.reservas.availability.mapper;

import com.reservas.commons.dto.TimeSlotCreateDto;
import com.reservas.commons.dto.TimeSlotDto;
import com.reservas.commons.dto.TimeSlotStatusDto;
import com.reservas.commons.entity.TimeSlot;
import com.reservas.commons.enums.TimeSlotStatus;
import com.reservas.availability.dto.request.TimeSlotUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

/** MapStruct: {@link TimeSlot} ↔ DTOs commons y peticiones de actualización. */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface TimeSlotMapper {

	@Mapping(target = "professorId", source = "professor.id")
	@Mapping(target = "startTime", source = "startTime", qualifiedByName = "localTimeToString")
	@Mapping(target = "endTime", source = "endTime", qualifiedByName = "localTimeToString")
	@Mapping(target = "status", source = "status", qualifiedByName = "statusToDto")
	TimeSlotDto toDto(TimeSlot entity);

	@Named("localTimeToString")
	default String localTimeToString(java.time.LocalTime time) {
		return time == null ? null : time.toString();
	}

	@Named("statusToDto")
	default TimeSlotStatusDto statusToDto(TimeSlotStatus status) {
		return status == null ? null : TimeSlotStatusDto.fromValue(status.name());
	}

	default TimeSlotStatus statusFromDto(TimeSlotStatusDto status) {
		return status == null ? null : TimeSlotStatus.valueOf(status.name());
	}

	default java.time.LocalTime parseTime(String value) {
		return value == null ? null : java.time.LocalTime.parse(value);
	}

	default void applyCreate(TimeSlotCreateDto dto, TimeSlot entity) {
		entity.setSlotDate(dto.getSlotDate());
		entity.setStartTime(parseTime(dto.getStartTime()));
		entity.setEndTime(parseTime(dto.getEndTime()));
	}

	default void applyUpdate(TimeSlotUpdateRequest request, TimeSlot entity) {
		entity.setSlotDate(request.getSlotDate());
		entity.setStartTime(parseTime(request.getStartTime()));
		entity.setEndTime(parseTime(request.getEndTime()));
	}
}
