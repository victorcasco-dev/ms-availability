package com.reservas.security;

/**
 * Roles de acceso. Un usuario puede tener varios a la vez.
 * La autorización usa el nombre del enum como authority (sin prefijo {@code ROLE_}).
 */
public enum Role {
	STUDENT,
	PROFESSOR,
	ADMINISTRATOR
}
