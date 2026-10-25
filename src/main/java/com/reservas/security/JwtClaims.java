package com.reservas.security;

import java.util.List;
import java.util.UUID;

/** Claims verificados de un access token. */
public record JwtClaims(UUID userId, String email, List<String> roles) {
}
