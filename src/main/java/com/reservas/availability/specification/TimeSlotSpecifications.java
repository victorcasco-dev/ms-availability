package com.reservas.availability.specification;

import com.reservas.commons.dto.TimeSlotStatusDto;
import com.reservas.commons.entity.Professor;
import com.reservas.commons.entity.ProfessorResearchLine;
import com.reservas.commons.entity.TimeSlot;
import com.reservas.commons.enums.TimeSlotStatus;
import com.reservas.availability.dto.request.TimeSlotSearchRequest;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public final class TimeSlotSpecifications {

	private TimeSlotSpecifications() {
	}

	public static Specification<TimeSlot> from(TimeSlotSearchRequest request) {
		return (root, query, cb) -> {
			if (query != null) {
				query.distinct(true);
			}
			List<Predicate> predicates = new ArrayList<>();

			if (request.getProfessorId() != null) {
				predicates.add(cb.equal(root.get("professor").get("id"), request.getProfessorId()));
			}
			if (request.getResearchLineId() != null) {
				Join<TimeSlot, Professor> professor = root.join("professor", JoinType.INNER);
				Join<Professor, ProfessorResearchLine> lines = professor.join("researchLines", JoinType.INNER);
				predicates.add(cb.equal(lines.get("researchLine").get("id"), request.getResearchLineId()));
			}
			if (request.getSlotDate() != null) {
				predicates.add(cb.equal(root.get("slotDate"), request.getSlotDate()));
			}
			if (Boolean.TRUE.equals(request.getOnlyAvailable())) {
				predicates.add(cb.equal(root.get("status"), TimeSlotStatus.AVAILABLE));
			} else if (request.getStatus() != null) {
				predicates.add(cb.equal(root.get("status"), toEntityStatus(request.getStatus())));
			} else {
				predicates.add(cb.notEqual(root.get("status"), TimeSlotStatus.CANCELLED));
			}

			return cb.and(predicates.toArray(Predicate[]::new));
		};
	}

	private static TimeSlotStatus toEntityStatus(TimeSlotStatusDto status) {
		return TimeSlotStatus.valueOf(status.name());
	}
}
