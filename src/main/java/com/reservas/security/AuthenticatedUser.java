package com.reservas.security;

import java.util.List;
import java.util.UUID;

/**
 * Principal autenticado a partir del JWT. No incluye la contraseña.
 */
public record AuthenticatedUser(UUID id, String email, List<String> roles) {
}
