package com.reservas.availability.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Cuerpo de {@code POST /time-slots/batch}: cabecera (profesor + fecha) y detalle (franjas horarias).
 * Todo se guarda en una sola transacción; si una franja falla no se guarda ninguna.
 */
@Getter
@Setter
public class TimeSlotBatchCreateRequest {

	@NotNull
	private UUID professorId;

	@NotNull
	private LocalDate slotDate;

	@Valid
	@NotEmpty
	@Size(max = 48)
	private List<SlotRange> slots = new ArrayList<>();

	@Getter
	@Setter
	public static class SlotRange {

		@NotBlank
		private String startTime;

		@NotBlank
		private String endTime;
	}
}
