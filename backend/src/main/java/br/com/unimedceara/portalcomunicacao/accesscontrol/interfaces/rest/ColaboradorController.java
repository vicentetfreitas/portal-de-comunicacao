package br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest;

import br.com.unimedceara.portalcomunicacao.accesscontrol.application.service.ColaboradorApplicationService;
import br.com.unimedceara.portalcomunicacao.accesscontrol.domain.model.ColaboradorStatus;
import br.com.unimedceara.portalcomunicacao.accesscontrol.domain.model.JwtAuthenticatedPrincipal;
import br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest.dto.ColaboradorResponse;
import br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest.dto.CreateColaboradorRequest;
import br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest.dto.UpdateColaboradorRequest;
import br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest.dto.UpdateColaboradorStatusRequest;
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
 * Endpoints REST da Feature FT-COLABORADOR.
 */
@RestController
@RequestMapping("/api/v1/colaboradores")
@ConditionalOnProperty(prefix = "application.persistence", name = "enabled", havingValue = "true", matchIfMissing = true)
@Tag(name = "Colaboradores", description = "Gestão administrativa de colaboradores")
public class ColaboradorController {

    private final ColaboradorApplicationService colaboradorApplicationService;

    public ColaboradorController(ColaboradorApplicationService colaboradorApplicationService) {
        this.colaboradorApplicationService = colaboradorApplicationService;
    }

    @PostMapping
    @Operation(summary = "Criar colaborador", description = "Cadastra colaborador com vínculo organizacional")
    public ResponseEntity<ApiResponse<ColaboradorResponse>> create(
            @Valid @RequestBody CreateColaboradorRequest request,
            @AuthenticationPrincipal JwtAuthenticatedPrincipal principal) {
        ColaboradorResponse response = colaboradorApplicationService.create(request, principal.colaboradorId());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar colaborador", description = "Retorna colaborador por identificador")
    public ApiResponse<ColaboradorResponse> findById(@PathVariable Long id) {
        return ApiResponse.success(colaboradorApplicationService.findById(id));
    }

    @GetMapping
    @Operation(summary = "Listar colaboradores", description = "Listagem paginada com filtros corporativos")
    public ApiResponse<PageResponse<ColaboradorResponse>> list(
            @RequestParam(required = false) ColaboradorStatus status,
            @RequestParam(required = false) Long singularId,
            @RequestParam(required = false) Long areaId,
            @RequestParam(required = false) Long teamId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @PageableDefault(size = 20, sort = "nome") Pageable pageable) {
        return ApiResponse.success(colaboradorApplicationService.list(
                status, singularId, areaId, teamId, name, email, pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar colaborador", description = "Atualiza dados cadastrais (e-mail imutável)")
    public ApiResponse<ColaboradorResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateColaboradorRequest request,
            @AuthenticationPrincipal JwtAuthenticatedPrincipal principal) {
        return ApiResponse.success(colaboradorApplicationService.update(id, request, principal.colaboradorId()));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Alterar status", description = "Ativa ou inativa logicamente o colaborador")
    public ApiResponse<ColaboradorResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateColaboradorStatusRequest request,
            @AuthenticationPrincipal JwtAuthenticatedPrincipal principal) {
        return ApiResponse.success(colaboradorApplicationService.updateStatus(id, request, principal.colaboradorId()));
    }
}
