package br.com.unimedceara.portalcomunicacao.infrastructure.security.entrypoint;

import br.com.unimedceara.portalcomunicacao.shared.dto.ErrorResponse;
import br.com.unimedceara.portalcomunicacao.shared.exception.UnauthorizedException;
import br.com.unimedceara.portalcomunicacao.shared.exception.ExceptionResponseBuilder;
import br.com.unimedceara.portalcomunicacao.shared.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;

/**
 * Responde requisições não autenticadas com {@link ErrorResponse} padronizado (401).
 */
@Component
@RequiredArgsConstructor
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final String UNAUTHORIZED_MESSAGE = "Authentication required";

    private final ExceptionResponseBuilder responseBuilder;
    private final JsonMapper jsonMapper;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException {
        ErrorResponse body = responseBuilder.buildErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                UnauthorizedException.ERROR_CODE,
                UNAUTHORIZED_MESSAGE,
                request.getRequestURI());

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        jsonMapper.writeValue(response.getOutputStream(), body);
    }
}
