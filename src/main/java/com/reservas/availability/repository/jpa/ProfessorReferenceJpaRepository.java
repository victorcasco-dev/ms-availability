package com.reservas.availability.repository.jpa;

import com.reservas.commons.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProfessorReferenceJpaRepository extends JpaRepository<Professor, UUID> {
}
