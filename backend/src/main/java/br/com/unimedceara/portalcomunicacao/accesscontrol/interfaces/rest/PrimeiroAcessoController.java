package br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest;

import br.com.unimedceara.portalcomunicacao.accesscontrol.application.service.PrimeiroAcessoApplicationService;
import br.com.unimedceara.portalcomunicacao.accesscontrol.domain.model.JwtAuthenticatedPrincipal;
import br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest.dto.AuthenticatedUserResponse;
import br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest.dto.CompletePrimeiroAcessoRequest;
import br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest.dto.PrimeiroAcessoAreaResponse;
import br.com.unimedceara.portalcomunicacao.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Onboarding de Primeiro Acesso (FT-PRIMEIRO-ACESSO).
 */
@RestController
@RequestMapping("/api/v1/auth/primeiro-acesso")
@ConditionalOnProperty(prefix = "application.persistence", name = "enabled", havingValue = "true", matchIfMissing = true)
@Tag(name = "Primeiro Acesso", description = "Seleção de área e conclusão do vínculo organizacional")
public class PrimeiroAcessoController {

    private final PrimeiroAcessoApplicationService primeiroAcessoApplicationService;

    public PrimeiroAcessoController(PrimeiroAcessoApplicationService primeiroAcessoApplicationService) {
        this.primeiroAcessoApplicationService = primeiroAcessoApplicationService;
    }

    @GetMapping("/areas")
    @Operation(summary = "Listar áreas", description = "Áreas ativas da Singular resolvida pelo domínio autenticado")
    public ApiResponse<List<PrimeiroAcessoAreaResponse>> listAreas(
            @AuthenticationPrincipal JwtAuthenticatedPrincipal principal) {
        return ApiResponse.success(primeiroAcessoApplicationService.listAreas(principal));
    }

    @PostMapping
    @Operation(summary = "Concluir primeiro acesso", description = "Cria o COLABORADOR e promove a sessão operacional")
    public ApiResponse<AuthenticatedUserResponse> complete(
            @Valid @RequestBody CompletePrimeiroAcessoRequest request,
            @AuthenticationPrincipal JwtAuthenticatedPrincipal principal,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {
        return ApiResponse.success(
                primeiroAcessoApplicationService.complete(principal, request, httpRequest, httpResponse));
    }
}
