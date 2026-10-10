package com.reservas.availability.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TimeSlotUpdateRequest {

	@NotNull
	private LocalDate slotDate;

	@NotNull
	private String startTime;

	@NotNull
	private String endTime;
}
