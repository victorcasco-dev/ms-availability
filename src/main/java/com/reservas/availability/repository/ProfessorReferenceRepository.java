package com.reservas.availability.repository;

import com.reservas.commons.entity.Professor;
import com.reservas.availability.repository.jpa.ProfessorReferenceJpaRepository;
import com.reservas.commons.exception.InvalidOperationException;
import com.reservas.commons.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio de apoyo sobre {@link Professor} (solo lectura en este microservicio).
 */
@Repository
@RequiredArgsConstructor
public class ProfessorReferenceRepository {

	private static final Logger log = LoggerFactory.getLogger(ProfessorReferenceRepository.class);

	private final ProfessorReferenceJpaRepository jpa;

	public Optional<Professor> findById(UUID id) {
		log.debug("Finding professor reference id={}", id);
		return jpa.findById(id);
	}

	public Page<Professor> search(Specification<Professor> specification, Pageable pageable) {
		log.debug("Searching professor references page={} size={}", pageable.getPageNumber(), pageable.getPageSize());
		return jpa.findAll(specification, pageable);
	}

	public Professor getActiveProfessor(UUID id) {
		log.debug("Loading professor reference id={}", id);
		Professor professor = jpa.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Professor not found: " + id));
		if (!professor.isActive()) {
			throw new InvalidOperationException("Professor is inactive: " + id);
		}
		return professor;
	}
}
