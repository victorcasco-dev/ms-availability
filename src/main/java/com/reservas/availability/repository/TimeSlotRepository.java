package com.reservas.availability.repository;

import com.reservas.commons.entity.TimeSlot;
import com.reservas.availability.repository.jpa.TimeSlotJpaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TimeSlotRepository {

	private static final Logger log = LoggerFactory.getLogger(TimeSlotRepository.class);

	private final TimeSlotJpaRepository jpa;

	public Optional<TimeSlot> findById(UUID id) {
		log.debug("Finding time slot id={}", id);
		return jpa.findById(id);
	}

	public Page<TimeSlot> search(Specification<TimeSlot> specification, Pageable pageable) {
		log.debug("Searching time slots page={} size={}", pageable.getPageNumber(), pageable.getPageSize());
		return jpa.findAll(specification, pageable);
	}

	public boolean existsByProfessorDateAndStart(UUID professorId, LocalDate slotDate, LocalTime startTime) {
		log.debug("Checking slot uniqueness professorId={} date={} start={}", professorId, slotDate, startTime);
		return jpa.existsByProfessor_IdAndSlotDateAndStartTime(professorId, slotDate, startTime);
	}

	public boolean existsByProfessorDateAndStartExcludingId(
			UUID professorId,
			LocalDate slotDate,
			LocalTime startTime,
			UUID id
	) {
		log.debug("Checking slot uniqueness excluding id={}", id);
		return jpa.existsByProfessor_IdAndSlotDateAndStartTimeAndIdNot(professorId, slotDate, startTime, id);
	}

	public TimeSlot save(TimeSlot timeSlot) {
		log.debug("Saving time slot id={} professorId={}", timeSlot.getId(), timeSlot.getProfessor().getId());
		return jpa.save(timeSlot);
	}
}
