package com.reservas.availability.service;

import com.reservas.commons.dto.TimeSlotCreateDto;
import com.reservas.commons.dto.TimeSlotDto;
import com.reservas.commons.dto.TimeSlotPageDto;
import com.reservas.commons.entity.Professor;
import com.reservas.commons.entity.TimeSlot;
import com.reservas.commons.enums.TimeSlotStatus;
import com.reservas.commons.exception.BadRequestException;
import com.reservas.commons.exception.ConflictException;
import com.reservas.commons.exception.InvalidOperationException;
import com.reservas.commons.exception.ResourceNotFoundException;
import com.reservas.commons.pagination.PageRequestFactory;
import com.reservas.availability.cache.CacheKeyFactory;
import com.reservas.availability.cache.IndividualCache;
import com.reservas.availability.dto.request.TimeSlotSearchRequest;
import com.reservas.availability.dto.request.TimeSlotUpdateRequest;
import com.reservas.availability.mapper.PageMapper;
import com.reservas.availability.mapper.TimeSlotMapper;
import com.reservas.availability.repository.ProfessorReferenceRepository;
import com.reservas.availability.repository.TimeSlotRepository;
import com.reservas.availability.specification.TimeSlotSpecifications;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

/**
 * Reglas de negocio de franjas horarias (disponible, reservada, cancelada).
 */
@Service
@RequiredArgsConstructor
public class TimeSlotService {

	private static final Logger log = LoggerFactory.getLogger(TimeSlotService.class);

	private final TimeSlotRepository timeSlotRepository;
	private final ProfessorReferenceRepository professorReferenceRepository;
	private final TimeSlotMapper timeSlotMapper;
	private final PageMapper pageMapper;
	private final PageRequestFactory pageRequestFactory;
	private final IndividualCache individualCache;
	private final CacheKeyFactory cacheKeys;

	@Transactional
	@CachePut(cacheNames = "timeSlots", key = "@cacheKeys.of('time-slot', #result.id)")
	public TimeSlotDto create(TimeSlotCreateDto dto) {
		log.info("Creating time slot professorId={} date={}", dto.getProfessorId(), dto.getSlotDate());
		validateCreate(dto);
		Professor professor = professorReferenceRepository.getActiveProfessor(dto.getProfessorId());
		LocalTime start = timeSlotMapper.parseTime(dto.getStartTime());
		LocalTime end = timeSlotMapper.parseTime(dto.getEndTime());
		validateTimeRange(start, end);
		if (timeSlotRepository.existsByProfessorDateAndStart(professor.getId(), dto.getSlotDate(), start)) {
			throw new ConflictException("Time slot already exists for this professor, date and start time");
		}

		TimeSlot entity = new TimeSlot();
		entity.setProfessor(professor);
		timeSlotMapper.applyCreate(dto, entity);
		return timeSlotMapper.toDto(timeSlotRepository.save(entity));
	}

	@Transactional(readOnly = true)
	@Cacheable(cacheNames = "timeSlots", key = "@cacheKeys.of('time-slot', #id)")
	public TimeSlotDto getById(UUID id) {
		log.info("Getting time slot id={}", id);
		return timeSlotMapper.toDto(findOrThrow(id));
	}

	@Transactional(readOnly = true)
	public TimeSlotPageDto search(TimeSlotSearchRequest request) {
		log.info("Searching time slots professorId={} researchLineId={} date={}",
				request.getProfessorId(), request.getResearchLineId(), request.getSlotDate());
		Page<TimeSlot> page = timeSlotRepository.search(
				TimeSlotSpecifications.from(request),
				pageRequestFactory.from(
						request.getPage(),
						request.getSize(),
						Sort.by("slotDate").descending().and(Sort.by("startTime").ascending())
				)
		);
		TimeSlotPageDto result = pageMapper.toTimeSlotPage(page);
		individualCache.putEach("timeSlots", result.getContent(), item -> cacheKeys.of("time-slot", item.getId()));
		return result;
	}

	@Transactional
	@CachePut(cacheNames = "timeSlots", key = "@cacheKeys.of('time-slot', #id)")
	public TimeSlotDto update(UUID id, TimeSlotUpdateRequest request) {
		log.info("Updating time slot id={}", id);
		TimeSlot entity = findOrThrow(id);
		if (entity.getStatus() != TimeSlotStatus.AVAILABLE) {
			throw new InvalidOperationException("Only available time slots can be updated");
		}
		LocalTime start = timeSlotMapper.parseTime(request.getStartTime());
		LocalTime end = timeSlotMapper.parseTime(request.getEndTime());
		validateTimeRange(start, end);
		if (timeSlotRepository.existsByProfessorDateAndStartExcludingId(
				entity.getProfessor().getId(),
				request.getSlotDate(),
				start,
				id
		)) {
			throw new ConflictException("Time slot already exists for this professor, date and start time");
		}
		timeSlotMapper.applyUpdate(request, entity);
		return timeSlotMapper.toDto(timeSlotRepository.save(entity));
	}

	@Transactional
	@CacheEvict(cacheNames = "timeSlots", key = "@cacheKeys.of('time-slot', #id)")
	public void deleteLogically(UUID id) {
		log.info("Logical delete (cancel) time slot id={}", id);
		TimeSlot entity = findOrThrow(id);
		entity.cancel();
		timeSlotRepository.save(entity);
	}

	@Transactional
	@CachePut(cacheNames = "timeSlots", key = "@cacheKeys.of('time-slot', #id)")
	public TimeSlotDto book(UUID id) {
		log.info("Booking time slot id={}", id);
		TimeSlot entity = findOrThrow(id);
		entity.book();
		return timeSlotMapper.toDto(timeSlotRepository.save(entity));
	}

	@Transactional
	@CachePut(cacheNames = "timeSlots", key = "@cacheKeys.of('time-slot', #id)")
	public TimeSlotDto release(UUID id) {
		log.info("Releasing time slot id={}", id);
		TimeSlot entity = findOrThrow(id);
		entity.release();
		return timeSlotMapper.toDto(timeSlotRepository.save(entity));
	}

	private void validateCreate(TimeSlotCreateDto dto) {
		if (dto.getProfessorId() == null) {
			throw new BadRequestException("professorId is required");
		}
		if (dto.getSlotDate() == null) {
			throw new BadRequestException("slotDate is required");
		}
		if (dto.getStartTime() == null || dto.getEndTime() == null) {
			throw new BadRequestException("startTime and endTime are required");
		}
		if (dto.getSlotDate().isBefore(LocalDate.now())) {
			throw new BadRequestException("slotDate cannot be in the past");
		}
	}

	private void validateTimeRange(LocalTime start, LocalTime end) {
		if (start == null || end == null || !end.isAfter(start)) {
			throw new BadRequestException("endTime must be after startTime");
		}
	}

	private TimeSlot findOrThrow(UUID id) {
		return timeSlotRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Time slot not found: " + id));
	}
}
