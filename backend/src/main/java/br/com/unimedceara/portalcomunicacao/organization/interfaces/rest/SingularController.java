package br.com.unimedceara.portalcomunicacao.organization.interfaces.rest;

import br.com.unimedceara.portalcomunicacao.accesscontrol.domain.model.JwtAuthenticatedPrincipal;
import br.com.unimedceara.portalcomunicacao.organization.application.service.SingularApplicationService;
import br.com.unimedceara.portalcomunicacao.organization.domain.model.SingularStatus;
import br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto.CreateSingularRequest;
import br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto.SingularResponse;
import br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto.UpdateSingularRequest;
import br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto.UpdateSingularStatusRequest;
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
 * Endpoints REST da Feature FT-SINGULAR.
 */
@RestController
@RequestMapping("/api/v1/singulares")
@ConditionalOnProperty(prefix = "application.persistence", name = "enabled", havingValue = "true", matchIfMissing = true)
@Tag(name = "Singulares", description = "Gestão de singulares organizacionais")
public class SingularController {

    private final SingularApplicationService singularApplicationService;

    public SingularController(SingularApplicationService singularApplicationService) {
        this.singularApplicationService = singularApplicationService;
    }

    @PostMapping
    @Operation(summary = "Criar singular")
    public ResponseEntity<ApiResponse<SingularResponse>> create(
            @Valid @RequestBody CreateSingularRequest request,
            @AuthenticationPrincipal JwtAuthenticatedPrincipal principal) {
        SingularResponse response = singularApplicationService.create(request, principal.colaboradorId());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar singular")
    public ApiResponse<SingularResponse> findById(@PathVariable Long id) {
        return ApiResponse.success(singularApplicationService.findById(id));
    }

    @GetMapping
    @Operation(summary = "Listar singulares")
    public ApiResponse<PageResponse<SingularResponse>> list(
            @RequestParam(required = false) SingularStatus status,
            @RequestParam(required = false) Long federationId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String acronym,
            @RequestParam(required = false) String unimedCode,
            @PageableDefault(size = 20, sort = "nome") Pageable pageable) {
        return ApiResponse.success(
                singularApplicationService.list(status, federationId, name, acronym, unimedCode, pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar singular")
    public ApiResponse<SingularResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSingularRequest request,
            @AuthenticationPrincipal JwtAuthenticatedPrincipal principal) {
        return ApiResponse.success(singularApplicationService.update(id, request, principal.colaboradorId()));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Alterar status da singular")
    public ApiResponse<SingularResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSingularStatusRequest request,
            @AuthenticationPrincipal JwtAuthenticatedPrincipal principal) {
        return ApiResponse.success(singularApplicationService.updateStatus(id, request, principal.colaboradorId()));
    }
}
