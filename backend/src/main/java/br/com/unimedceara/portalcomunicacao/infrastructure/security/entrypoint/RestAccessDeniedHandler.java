package br.com.unimedceara.portalcomunicacao.infrastructure.security.entrypoint;

import br.com.unimedceara.portalcomunicacao.shared.dto.ErrorResponse;
import br.com.unimedceara.portalcomunicacao.shared.exception.ExceptionResponseBuilder;
import br.com.unimedceara.portalcomunicacao.shared.exception.ForbiddenException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;

/**
 * Responde requisições autenticadas sem autorização com {@link ErrorResponse} padronizado (403).
 */
@Component
@RequiredArgsConstructor
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    private static final String FORBIDDEN_MESSAGE = "Access denied";

    private final ExceptionResponseBuilder responseBuilder;
    private final JsonMapper jsonMapper;

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException {
        ErrorResponse body = responseBuilder.buildErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                ForbiddenException.ERROR_CODE,
                FORBIDDEN_MESSAGE,
                request.getRequestURI());

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        jsonMapper.writeValue(response.getOutputStream(), body);
    }
}
