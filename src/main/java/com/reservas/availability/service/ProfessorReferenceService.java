package com.reservas.availability.service;

import com.reservas.commons.entity.Professor;
import com.reservas.commons.exception.ResourceNotFoundException;
import com.reservas.commons.pagination.PageRequestFactory;
import com.reservas.availability.dto.ProfessorReferenceDto;
import com.reservas.availability.dto.ProfessorReferencePageDto;
import com.reservas.availability.dto.request.ProfessorReferenceSearchRequest;
import com.reservas.availability.mapper.ProfessorReferenceMapper;
import com.reservas.availability.mapper.PageMapper;
import com.reservas.availability.repository.ProfessorReferenceRepository;
import com.reservas.availability.specification.ProfessorReferenceSpecifications;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Consulta de profesores activos al registrar franjas (entidad simple de apoyo, solo lectura).
 */
@Service
@RequiredArgsConstructor
public class ProfessorReferenceService {

	private static final Logger log = LoggerFactory.getLogger(ProfessorReferenceService.class);

	private final ProfessorReferenceRepository professorReferenceRepository;
	private final ProfessorReferenceMapper professorReferenceMapper;
	private final PageMapper pageMapper;
	private final PageRequestFactory pageRequestFactory;

	@Transactional(readOnly = true)
	public ProfessorReferenceDto getById(UUID id) {
		log.info("Getting professor reference id={}", id);
		return professorReferenceMapper.toDto(findOrThrow(id));
	}

	@Transactional(readOnly = true)
	public ProfessorReferencePageDto search(ProfessorReferenceSearchRequest request) {
		log.info("Searching professor references name={}", request.getName());
		Page<Professor> page = professorReferenceRepository.search(
				ProfessorReferenceSpecifications.from(request),
				pageRequestFactory.from(
						request.getPage(),
						request.getSize(),
						Sort.by("lastName").ascending().and(Sort.by("firstName").ascending())
				)
		);
		return pageMapper.toProfessorReferencePage(page);
	}

	private Professor findOrThrow(UUID id) {
		return professorReferenceRepository.findById(id)
				.filter(Professor::isActive)
				.orElseThrow(() -> new ResourceNotFoundException("Professor not found: " + id));
	}
}
