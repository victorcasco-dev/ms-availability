package com.reservas.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.util.StringUtils;

import java.util.List;

/** Aplica las reglas de URL declaradas en {@code app.security.rules} y {@code app.security.public-paths}. */
public final class UrlAuthorizationCustomizer {

	private UrlAuthorizationCustomizer() {
	}

	public static void apply(
			AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth,
			SecurityProperties properties
	) {
		for (String path : properties.getPublicPaths()) {
			if (StringUtils.hasText(path)) {
				auth.requestMatchers(PathPatternRequestMatcher.pathPattern(path.trim())).permitAll();
			}
		}
		for (SecurityProperties.AccessRule rule : properties.getRules()) {
			if (!StringUtils.hasText(rule.getPath())) {
				throw new IllegalStateException("app.security.rules path is required");
			}
			List<String> roles = normalize(rule.getRoles());
			if (roles.isEmpty()) {
				throw new IllegalStateException("app.security.rules roles are required for path " + rule.getPath());
			}
			String[] authorities = roles.toArray(String[]::new);
			List<String> methods = normalize(rule.getMethods());
			if (methods.isEmpty()) {
				auth.requestMatchers(PathPatternRequestMatcher.pathPattern(rule.getPath().trim()))
						.hasAnyAuthority(authorities);
				continue;
			}
			for (String method : methods) {
				auth.requestMatchers(PathPatternRequestMatcher.pathPattern(HttpMethod.valueOf(method), rule.getPath().trim()))
						.hasAnyAuthority(authorities);
			}
		}
		auth.anyRequest().authenticated();
	}

	private static List<String> normalize(List<String> values) {
		if (values == null) {
			return List.of();
		}
		return values.stream()
				.filter(StringUtils::hasText)
				.map(value -> value.trim().toUpperCase())
				.toList();
	}

}
