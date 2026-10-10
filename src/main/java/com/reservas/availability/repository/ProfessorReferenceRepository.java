package com.reservas.availability.repository;

import com.reservas.commons.entity.Professor;
import com.reservas.availability.repository.jpa.ProfessorReferenceJpaRepository;
import com.reservas.commons.exception.InvalidOperationException;
import com.reservas.commons.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProfessorReferenceRepository {

	private static final Logger log = LoggerFactory.getLogger(ProfessorReferenceRepository.class);

	private final ProfessorReferenceJpaRepository jpa;

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
