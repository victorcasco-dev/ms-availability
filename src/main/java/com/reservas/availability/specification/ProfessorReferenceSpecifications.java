package com.reservas.availability.specification;

import com.reservas.commons.entity.Professor;
import com.reservas.availability.dto.request.ProfessorReferenceSearchRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Filtros JPA para listar profesores activos.
 */
public final class ProfessorReferenceSpecifications {

	private ProfessorReferenceSpecifications() {
	}

	public static Specification<Professor> from(ProfessorReferenceSearchRequest request) {
		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(cb.isTrue(root.get("active")));
			if (StringUtils.hasText(request.getName())) {
				String like = "%" + request.getName().trim().toLowerCase() + "%";
				predicates.add(cb.or(
						cb.like(cb.lower(root.get("firstName")), like),
						cb.like(cb.lower(root.get("lastName")), like)
				));
			}
			return cb.and(predicates.toArray(Predicate[]::new));
		};
	}
}
