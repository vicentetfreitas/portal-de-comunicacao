package br.com.unimedceara.portalcomunicacao.documento.interfaces.rest;

import br.com.unimedceara.portalcomunicacao.accesscontrol.domain.model.JwtAuthenticatedPrincipal;
import br.com.unimedceara.portalcomunicacao.documento.application.service.PastaApplicationService;
import br.com.unimedceara.portalcomunicacao.documento.interfaces.rest.dto.PastaResponse;
import br.com.unimedceara.portalcomunicacao.shared.dto.ApiResponse;
import br.com.unimedceara.portalcomunicacao.shared.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints REST da Feature FT-DOCUMENTO — pastas.
 */
@RestController
@RequestMapping("/api/v1/pastas")
@ConditionalOnProperty(prefix = "application.persistence", name = "enabled", havingValue = "true", matchIfMissing = true)
@Tag(name = "Pastas", description = "Consulta de pastas e documentos acessíveis ao Contexto Ativo")
public class PastaController {

    private final PastaApplicationService pastaApplicationService;

    public PastaController(PastaApplicationService pastaApplicationService) {
        this.pastaApplicationService = pastaApplicationService;
    }

    @GetMapping
    @Operation(
            summary = "Listar pastas",
            description = "Lista pastas e documentos com permissão para o Contexto Ativo do colaborador")
    public ApiResponse<PageResponse<PastaResponse>> list(
            @AuthenticationPrincipal JwtAuthenticatedPrincipal principal,
            @PageableDefault(size = 20, sort = "nome") Pageable pageable) {
        return ApiResponse.success(pastaApplicationService.list(principal, pageable));
    }
}
