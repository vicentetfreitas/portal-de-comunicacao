package br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest;

import br.com.unimedceara.portalcomunicacao.accesscontrol.application.service.AuthenticationService;
import br.com.unimedceara.portalcomunicacao.accesscontrol.domain.model.JwtAuthenticatedPrincipal;
import br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest.dto.AuthenticatedUserResponse;
import br.com.unimedceara.portalcomunicacao.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * Endpoints de autenticação FT-AUTH.
 */
@RestController
@RequestMapping("/api/v1/auth")
@ConditionalOnProperty(prefix = "application.persistence", name = "enabled", havingValue = "true", matchIfMissing = true)
@Tag(name = "Authentication", description = "Autenticação stateless via Zimbra, JWT e Refresh Token")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Inicia login redirecionando ao Zimbra.
     */
    @GetMapping("/login")
    @Operation(summary = "Iniciar login", description = "Redireciona ao Zimbra com state anti-CSRF")
    public ResponseEntity<Void> login(
            @RequestParam(name = "remember_me", defaultValue = "false") boolean rememberMe,
            HttpServletRequest request) {
        URI redirectUrl = authenticationService.initiateLogin(rememberMe, request);
        return ResponseEntity.status(HttpStatus.FOUND).location(redirectUrl).build();
    }

    /**
     * Processa callback do Zimbra após autenticação.
     */
    @GetMapping("/callback")
    @Operation(summary = "Callback Zimbra", description = "Valida identidade, emite cookies e redireciona ao frontend")
    public ResponseEntity<Void> callback(
            @RequestParam(name = "token", required = false) String token,
            @RequestParam(name = "state", required = false) String state,
            HttpServletRequest request,
            HttpServletResponse response) {
        URI redirectUrl = authenticationService.handleCallback(token, state, request, response);
        return ResponseEntity.status(HttpStatus.FOUND).location(redirectUrl).build();
    }

    /**
     * Retorna identidade do colaborador autenticado.
     */
    @GetMapping("/me")
    @Operation(summary = "Identidade autenticada", description = "Retorna dados do colaborador e permissões")
    public ApiResponse<AuthenticatedUserResponse> me(@AuthenticationPrincipal JwtAuthenticatedPrincipal principal) {
        return ApiResponse.success(authenticationService.getAuthenticatedUser(principal));
    }

    /**
     * Renova Access Token via Refresh Token.
     */
    @PostMapping("/refresh")
    @Operation(summary = "Renovar Access Token", description = "Renova JWT a partir do Refresh Token em cookie")
    public ApiResponse<Void> refresh(HttpServletRequest request, HttpServletResponse response) {
        authenticationService.refreshAccessToken(request.getCookies(), response);
        return ApiResponse.success("Access token renovado", null);
    }

    /**
     * Encerra sessão revogando Refresh Token e removendo cookies.
     */
    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Revoga sessão e remove cookies de autenticação")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        authenticationService.logout(request.getCookies(), response);
        return ResponseEntity.noContent().build();
    }
}
