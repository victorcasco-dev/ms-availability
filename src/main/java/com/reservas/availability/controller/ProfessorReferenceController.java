package com.reservas.availability.controller;

import com.reservas.availability.dto.ProfessorReferenceDto;
import com.reservas.availability.dto.ProfessorReferencePageDto;
import com.reservas.availability.dto.request.ProfessorReferenceSearchRequest;
import com.reservas.availability.service.ProfessorReferenceService;
import com.reservas.commons.web.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Profesores en solo lectura (segunda entidad simple expuesta en este microservicio).
 */
@RestController
@RequestMapping("/professor-references")
@RequiredArgsConstructor
@Tag(name = "Professor references")
public class ProfessorReferenceController extends BaseController {

	private final ProfessorReferenceService professorReferenceService;

	@GetMapping
	@PreAuthorize("isAuthenticated()")
	@Operation(summary = "List active professors with pagination")
	public ProfessorReferencePageDto list(
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size,
			@RequestParam(required = false) String name
	) {
		log.info("Incoming GET /professor-references");
		ProfessorReferenceSearchRequest request = new ProfessorReferenceSearchRequest();
		request.setPage(page);
		request.setSize(size);
		request.setName(name);
		return professorReferenceService.search(request);
	}

	@PostMapping("/search")
	@PreAuthorize("isAuthenticated()")
	@Operation(summary = "Search professors with pagination in the request body")
	public ProfessorReferencePageDto search(@Valid @RequestBody ProfessorReferenceSearchRequest request) {
		log.info("Incoming POST /professor-references/search");
		return professorReferenceService.search(request);
	}

	@GetMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	@Operation(summary = "Get professor reference by id")
	public ProfessorReferenceDto getById(@PathVariable UUID id) {
		log.info("Incoming GET /professor-references/{}", id);
		return professorReferenceService.getById(id);
	}
}
