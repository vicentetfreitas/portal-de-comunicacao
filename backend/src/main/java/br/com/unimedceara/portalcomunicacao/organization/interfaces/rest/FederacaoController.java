package br.com.unimedceara.portalcomunicacao.organization.interfaces.rest;

import br.com.unimedceara.portalcomunicacao.accesscontrol.domain.model.JwtAuthenticatedPrincipal;
import br.com.unimedceara.portalcomunicacao.organization.application.service.FederacaoApplicationService;
import br.com.unimedceara.portalcomunicacao.organization.domain.model.FederacaoStatus;
import br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto.CreateFederacaoRequest;
import br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto.FederacaoResponse;
import br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto.UpdateFederacaoRequest;
import br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto.UpdateFederacaoStatusRequest;
import br.com.unimedceara.portalcomunicacao.shared.dto.ApiResponse;
import br.com.unimedceara.portalcomunicacao.shared.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints REST da Feature FT-FEDERACAO.
 */
@RestController
@RequestMapping("/api/v1/federacoes")
@ConditionalOnProperty(prefix = "application.persistence", name = "enabled", havingValue = "true", matchIfMissing = true)
@Tag(name = "Federações", description = "Gestão de federações organizacionais")
public class FederacaoController {

    private final FederacaoApplicationService federacaoApplicationService;

    public FederacaoController(FederacaoApplicationService federacaoApplicationService) {
        this.federacaoApplicationService = federacaoApplicationService;
    }

    @PostMapping
    @Operation(summary = "Criar federação")
    public ResponseEntity<ApiResponse<FederacaoResponse>> create(
            @Valid @RequestBody CreateFederacaoRequest request,
            @AuthenticationPrincipal JwtAuthenticatedPrincipal principal) {
        FederacaoResponse response = federacaoApplicationService.create(request, principal.colaboradorId());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar federação")
    public ApiResponse<FederacaoResponse> findById(@PathVariable Long id) {
        return ApiResponse.success(federacaoApplicationService.findById(id));
    }

    @GetMapping
    @Operation(summary = "Listar federações")
    public ApiResponse<PageResponse<FederacaoResponse>> list(
            @RequestParam(required = false) FederacaoStatus status,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String acronym,
            @RequestParam(required = false) Integer unimedCode,
            @PageableDefault(size = 20, sort = "nome") Pageable pageable) {
        return ApiResponse.success(
                federacaoApplicationService.list(status, name, acronym, unimedCode, pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar federação")
    public ApiResponse<FederacaoResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateFederacaoRequest request,
            @AuthenticationPrincipal JwtAuthenticatedPrincipal principal) {
        return ApiResponse.success(federacaoApplicationService.update(id, request, principal.colaboradorId()));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Alterar status da federação")
    public ApiResponse<FederacaoResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateFederacaoStatusRequest request,
            @AuthenticationPrincipal JwtAuthenticatedPrincipal principal) {
        return ApiResponse.success(federacaoApplicationService.updateStatus(id, request, principal.colaboradorId()));
    }
}
