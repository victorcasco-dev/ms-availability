package com.reservas.availability.repository.jpa;

import com.reservas.commons.entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

/** Spring Data JPA para {@link TimeSlot}. */
public interface TimeSlotJpaRepository extends JpaRepository<TimeSlot, UUID>, JpaSpecificationExecutor<TimeSlot> {

	boolean existsByProfessor_IdAndSlotDateAndStartTime(UUID professorId, LocalDate slotDate, LocalTime startTime);

	boolean existsByProfessor_IdAndSlotDateAndStartTimeAndIdNot(
			UUID professorId,
			LocalDate slotDate,
			LocalTime startTime,
			UUID id
	);
}
