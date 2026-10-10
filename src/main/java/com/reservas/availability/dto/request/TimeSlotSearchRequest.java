package com.reservas.availability.dto.request;

import com.reservas.commons.dto.TimeSlotStatusDto;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class TimeSlotSearchRequest {

	@Min(0)
	private Integer page;

	@Min(1)
	private Integer size;

	private UUID professorId;
	private UUID researchLineId;
	private LocalDate slotDate;
	private TimeSlotStatusDto status;

	/** When true, only slots in AVAILABLE status (ignores cancelled/booked). */
	private Boolean onlyAvailable;
}
