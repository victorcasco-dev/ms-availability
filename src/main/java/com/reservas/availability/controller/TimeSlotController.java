package com.reservas.availability.controller;

import com.reservas.commons.dto.TimeSlotCreateDto;
import com.reservas.commons.dto.TimeSlotDto;
import com.reservas.commons.dto.TimeSlotPageDto;
import com.reservas.commons.dto.TimeSlotStatusDto;
import com.reservas.availability.dto.request.TimeSlotSearchRequest;
import com.reservas.availability.dto.request.TimeSlotUpdateRequest;
import com.reservas.availability.service.TimeSlotService;
import com.reservas.commons.web.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;

/**
 * REST de {@link com.reservas.commons.entity.TimeSlot}.
 *
 * <p>DELETE realiza borrado lógico de negocio: {@code cancel()} deja el estado en {@code CANCELLED}
 * (Entrega #1 no define columna {@code active} en {@code time_slot}).</p>
 */
@RestController
@RequestMapping("/time-slots")
@RequiredArgsConstructor
@Tag(name = "Availability")
public class TimeSlotController extends BaseController {

	private final TimeSlotService timeSlotService;

	@PostMapping
	@PreAuthorize("hasAnyAuthority('PROFESSOR','ADMINISTRATOR')")
	@Operation(summary = "Create a professor time slot")
	public ResponseEntity<TimeSlotDto> create(@Valid @RequestBody TimeSlotCreateDto dto) {
		log.info("Incoming POST /time-slots");
		TimeSlotDto created = timeSlotService.create(dto);
		return created("/time-slots/" + created.getId(), created);
	}

	@GetMapping
	@PreAuthorize("isAuthenticated()")
	@Operation(summary = "Search time slots with query parameters (OpenAPI / gateway contract)")
	public TimeSlotPageDto searchByQuery(
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size,
			@RequestParam(required = false) UUID professorId,
			@RequestParam(required = false) UUID researchLineId,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
			@RequestParam(required = false) TimeSlotStatusDto status,
			@RequestParam(required = false) Boolean onlyAvailable
	) {
		log.info("Incoming GET /time-slots");
		TimeSlotSearchRequest request = new TimeSlotSearchRequest();
		request.setPage(page);
		request.setSize(size);
		request.setProfessorId(professorId);
		request.setResearchLineId(researchLineId);
		request.setSlotDate(date);
		request.setStatus(status);
		request.setOnlyAvailable(onlyAvailable);
		return timeSlotService.search(request);
	}

	@PostMapping("/search")
	@PreAuthorize("isAuthenticated()")
	@Operation(summary = "Search time slots with pagination and filters in the request body")
	public TimeSlotPageDto search(@Valid @RequestBody TimeSlotSearchRequest request) {
		log.info("Incoming POST /time-slots/search");
		return timeSlotService.search(request);
	}

	@GetMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	@Operation(summary = "Get time slot by id")
	public TimeSlotDto getById(@PathVariable UUID id) {
		log.info("Incoming GET /time-slots/{}", id);
		return timeSlotService.getById(id);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('PROFESSOR','ADMINISTRATOR')")
	@Operation(summary = "Update an available time slot")
	public TimeSlotDto update(@PathVariable UUID id, @Valid @RequestBody TimeSlotUpdateRequest request) {
		log.info("Incoming PUT /time-slots/{}", id);
		return timeSlotService.update(id, request);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('PROFESSOR','ADMINISTRATOR')")
	@Operation(summary = "Logical delete time slot (status = CANCELLED)")
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		log.info("Incoming DELETE /time-slots/{}", id);
		timeSlotService.deleteLogically(id);
		return noContent();
	}

	@PutMapping("/{id}/book")
	@PreAuthorize("hasAnyAuthority('STUDENT','PROFESSOR','ADMINISTRATOR')")
	@Operation(summary = "Mark time slot as booked (used by ms-reservas)")
	public TimeSlotDto book(@PathVariable UUID id) {
		log.info("Incoming PUT /time-slots/{}/book", id);
		return timeSlotService.book(id);
	}

	@PutMapping("/{id}/release")
	@PreAuthorize("hasAnyAuthority('STUDENT','PROFESSOR','ADMINISTRATOR')")
	@Operation(summary = "Release a booked time slot back to available")
	public TimeSlotDto release(@PathVariable UUID id) {
		log.info("Incoming PUT /time-slots/{}/release", id);
		return timeSlotService.release(id);
	}
}
