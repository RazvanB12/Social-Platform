package com.disi.social_platform_be.util;

import com.disi.social_platform_be.exception.InvalidTokenException;
import com.disi.social_platform_be.exception.UnauthorizedException;
import com.disi.social_platform_be.model.enums.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Component
@Data
public class AuthenticationInterceptor implements HandlerInterceptor {
    private static final Pattern PREFIX_PATTERN = Pattern.compile("\\QBearer\\E\\s*");
    private static final String BEARER = "Bearer";
    private static final String INVALID_TOKEN = "Invalid authentication token";
    private static final String UNAUTHORIZED_ACCESS  = "Unauthorized access";
    private static final List<String> NEED_AUTHORIZATION = List.of(
            "/pending-users",
            "activate-client-account",
            "reject-client-account",
            "/admin/delete-client-account",
            "/admin/delete-client-image",
            "/admin/image",
            "/admin/image/**",
            "admin/clients"
    );

    private final AuthenticationService authenticationService;
    private final TokenHolder tokenHolder;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        response.setStatus(HttpServletResponse.SC_OK);
        var jwt = extractJwtFromAuthHeader(request)
                .orElseThrow(() -> new InvalidTokenException(INVALID_TOKEN));
        if (!authenticationService.isValidToken(jwt)) {
            throw new InvalidTokenException(INVALID_TOKEN);
        }

        var role = authenticationService.extractRole(jwt);
        if (role.equals(Role.ADMIN)) {
            tokenHolder.setToken(jwt);
            return true;
        }

        if (NEED_AUTHORIZATION.stream().anyMatch(path -> request.getRequestURI().contains(path))) {
            throw new UnauthorizedException(UNAUTHORIZED_ACCESS);
        }

        tokenHolder.setToken(jwt);
        return true;
    }

    private Optional<String> extractJwtFromAuthHeader(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(authHeader -> authHeader.startsWith(BEARER))
                .map(authHeader -> PREFIX_PATTERN.matcher(authHeader).replaceFirst(""));
    }
}
