package br.com.unimedceara.portalcomunicacao.documento.interfaces.rest;

import br.com.unimedceara.portalcomunicacao.accesscontrol.domain.model.JwtAuthenticatedPrincipal;
import br.com.unimedceara.portalcomunicacao.documento.application.service.DocumentoApplicationService;
import br.com.unimedceara.portalcomunicacao.documento.application.service.DocumentoDownload;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints REST da Feature FT-DOCUMENTO — download de documentos.
 */
@RestController
@RequestMapping("/api/v1/documentos")
@ConditionalOnProperty(prefix = "application.persistence", name = "enabled", havingValue = "true", matchIfMissing = true)
@Tag(name = "Documentos", description = "Download de documentos")
public class DocumentoController {

    private final DocumentoApplicationService documentoApplicationService;

    public DocumentoController(DocumentoApplicationService documentoApplicationService) {
        this.documentoApplicationService = documentoApplicationService;
    }

    @GetMapping("/{id}/download")
    @Operation(
            summary = "Baixar documento",
            description = "Retorna o binário da versão atual do documento, mediado pelo backend (ADR-004)")
    public ResponseEntity<InputStreamResource> download(
            @PathVariable Long id,
            @AuthenticationPrincipal JwtAuthenticatedPrincipal principal) {
        DocumentoDownload download = documentoApplicationService.download(id, principal);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(download.tipoMime()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + download.nomeArquivo() + "\"")
                .contentLength(download.tamanhoBytes())
                .body(new InputStreamResource(download.conteudo()));
    }
}
