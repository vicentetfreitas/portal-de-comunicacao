package br.com.unimedceara.portalcomunicacao.organization.interfaces.rest;

import br.com.unimedceara.portalcomunicacao.accesscontrol.domain.model.JwtAuthenticatedPrincipal;
import br.com.unimedceara.portalcomunicacao.organization.application.service.EquipeApplicationService;
import br.com.unimedceara.portalcomunicacao.organization.domain.model.EquipeStatus;
import br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto.CreateEquipeRequest;
import br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto.EquipeResponse;
import br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto.UpdateEquipeRequest;
import br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto.UpdateEquipeStatusRequest;
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
 * Endpoints REST da Feature FT-EQUIPE.
 */
@RestController
@RequestMapping("/api/v1/equipes")
@ConditionalOnProperty(prefix = "application.persistence", name = "enabled", havingValue = "true", matchIfMissing = true)
@Tag(name = "Equipes", description = "Gestão de equipes organizacionais")
public class EquipeController {

    private final EquipeApplicationService equipeApplicationService;

    public EquipeController(EquipeApplicationService equipeApplicationService) {
        this.equipeApplicationService = equipeApplicationService;
    }

    @PostMapping
    @Operation(summary = "Criar equipe")
    public ResponseEntity<ApiResponse<EquipeResponse>> create(
            @Valid @RequestBody CreateEquipeRequest request,
            @AuthenticationPrincipal JwtAuthenticatedPrincipal principal) {
        EquipeResponse response = equipeApplicationService.create(request, principal.colaboradorId());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar equipe")
    public ApiResponse<EquipeResponse> findById(@PathVariable Long id) {
        return ApiResponse.success(equipeApplicationService.findById(id));
    }

    @GetMapping
    @Operation(summary = "Listar equipes")
    public ApiResponse<PageResponse<EquipeResponse>> list(
            @RequestParam(required = false) EquipeStatus status,
            @RequestParam(required = false) Long areaId,
            @RequestParam(required = false) String name,
            @PageableDefault(size = 20, sort = "nome") Pageable pageable) {
        return ApiResponse.success(equipeApplicationService.list(status, areaId, name, pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar equipe")
    public ApiResponse<EquipeResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEquipeRequest request,
            @AuthenticationPrincipal JwtAuthenticatedPrincipal principal) {
        return ApiResponse.success(equipeApplicationService.update(id, request, principal.colaboradorId()));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Alterar status da equipe")
    public ApiResponse<EquipeResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEquipeStatusRequest request,
            @AuthenticationPrincipal JwtAuthenticatedPrincipal principal) {
        return ApiResponse.success(equipeApplicationService.updateStatus(id, request, principal.colaboradorId()));
    }
}
