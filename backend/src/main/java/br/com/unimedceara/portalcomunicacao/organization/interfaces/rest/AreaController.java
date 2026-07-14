package br.com.unimedceara.portalcomunicacao.organization.interfaces.rest;

import br.com.unimedceara.portalcomunicacao.accesscontrol.domain.model.JwtAuthenticatedPrincipal;
import br.com.unimedceara.portalcomunicacao.organization.application.service.AreaApplicationService;
import br.com.unimedceara.portalcomunicacao.organization.domain.model.AreaStatus;
import br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto.AreaResponse;
import br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto.CreateAreaRequest;
import br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto.UpdateAreaRequest;
import br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto.UpdateAreaStatusRequest;
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
 * Endpoints REST da Feature FT-AREA.
 */
@RestController
@RequestMapping("/api/v1/areas")
@ConditionalOnProperty(prefix = "application.persistence", name = "enabled", havingValue = "true", matchIfMissing = true)
@Tag(name = "Areas", description = "Gestão de áreas organizacionais")
public class AreaController {

    private final AreaApplicationService areaApplicationService;

    public AreaController(AreaApplicationService areaApplicationService) {
        this.areaApplicationService = areaApplicationService;
    }

    @PostMapping
    @Operation(summary = "Criar área", description = "Cadastra nova área organizacional")
    public ResponseEntity<ApiResponse<AreaResponse>> create(
            @Valid @RequestBody CreateAreaRequest request,
            @AuthenticationPrincipal JwtAuthenticatedPrincipal principal) {
        AreaResponse response = areaApplicationService.create(request, principal.colaboradorId());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar área", description = "Retorna área por identificador")
    public ApiResponse<AreaResponse> findById(@PathVariable Long id) {
        return ApiResponse.success(areaApplicationService.findById(id));
    }

    @GetMapping
    @Operation(summary = "Listar áreas", description = "Listagem paginada com filtros corporativos")
    public ApiResponse<PageResponse<AreaResponse>> list(
            @RequestParam(required = false) AreaStatus status,
            @RequestParam(required = false) Long singularId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String acronym,
            @PageableDefault(size = 20, sort = "nome") Pageable pageable) {
        return ApiResponse.success(areaApplicationService.list(status, singularId, name, acronym, pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar área", description = "Atualiza dados cadastrais da área")
    public ApiResponse<AreaResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAreaRequest request,
            @AuthenticationPrincipal JwtAuthenticatedPrincipal principal) {
        return ApiResponse.success(areaApplicationService.update(id, request, principal.colaboradorId()));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Alterar status", description = "Ativa ou inativa logicamente a área")
    public ApiResponse<AreaResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAreaStatusRequest request,
            @AuthenticationPrincipal JwtAuthenticatedPrincipal principal) {
        return ApiResponse.success(areaApplicationService.updateStatus(id, request, principal.colaboradorId()));
    }
}
