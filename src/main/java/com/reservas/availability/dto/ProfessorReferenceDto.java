package com.reservas.availability.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Vista reducida de {@link com.reservas.commons.entity.Professor} para el módulo de disponibilidad.
 */
@Getter
@Setter
public class ProfessorReferenceDto {

	private UUID id;
	private String firstName;
	private String lastName;
	private String email;
	private String academicTitle;
	private boolean active;
}
