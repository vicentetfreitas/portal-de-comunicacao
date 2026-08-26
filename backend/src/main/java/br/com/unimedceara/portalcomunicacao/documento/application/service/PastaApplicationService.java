package br.com.unimedceara.portalcomunicacao.documento.application.service;

import br.com.unimedceara.portalcomunicacao.accesscontrol.domain.model.JwtAuthenticatedPrincipal;
import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.entity.ArquivoBinarioEntity;
import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.entity.DocumentoEntity;
import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.entity.DocumentoVersaoEntity;
import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.entity.PastaEntity;
import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.repository.ArquivoBinarioRepository;
import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.repository.DocumentoRepository;
import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.repository.DocumentoVersaoRepository;
import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.repository.PastaRepository;
import br.com.unimedceara.portalcomunicacao.documento.interfaces.rest.dto.DocumentoResponse;
import br.com.unimedceara.portalcomunicacao.documento.interfaces.rest.dto.PastaResponse;
import br.com.unimedceara.portalcomunicacao.documento.interfaces.rest.mapper.DocumentoMapper;
import br.com.unimedceara.portalcomunicacao.documento.interfaces.rest.mapper.PastaMapper;
import br.com.unimedceara.portalcomunicacao.shared.dto.PageResponse;
import br.com.unimedceara.portalcomunicacao.shared.util.PaginationUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Caso de uso de listagem de pastas/documentos (RF-DOCUMENTO-001, RF-DOCUMENTO-003,
 * RF-DOCUMENTO-004 — UC-DOCUMENTO-001/003/004).
 */
@Service
@ConditionalOnProperty(prefix = "application.persistence", name = "enabled", havingValue = "true", matchIfMissing = true)
public class PastaApplicationService {

    private final PastaRepository pastaRepository;
    private final DocumentoRepository documentoRepository;
    private final DocumentoVersaoRepository documentoVersaoRepository;
    private final ArquivoBinarioRepository arquivoBinarioRepository;
    private final PastaMapper pastaMapper;
    private final DocumentoMapper documentoMapper;

    public PastaApplicationService(
            PastaRepository pastaRepository,
            DocumentoRepository documentoRepository,
            DocumentoVersaoRepository documentoVersaoRepository,
            ArquivoBinarioRepository arquivoBinarioRepository,
            PastaMapper pastaMapper,
            DocumentoMapper documentoMapper) {
        this.pastaRepository = pastaRepository;
        this.documentoRepository = documentoRepository;
        this.documentoVersaoRepository = documentoVersaoRepository;
        this.arquivoBinarioRepository = arquivoBinarioRepository;
        this.pastaMapper = pastaMapper;
        this.documentoMapper = documentoMapper;
    }

    @Transactional(readOnly = true)
    public PageResponse<PastaResponse> list(JwtAuthenticatedPrincipal principal, Pageable pageable) {
        Pageable normalized = normalize(pageable);
        Page<PastaEntity> page = pastaRepository.findAccessible(
                principal.federationId(), principal.singularId(), principal.areaId(), principal.teamId(),
                normalized);

        List<PastaResponse> content = page.getContent().stream()
                .map(pasta -> pastaMapper.toResponse(pasta, documentosVisiveis(pasta.getId())))
                .toList();

        return PageResponse.of(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast());
    }

    private List<DocumentoResponse> documentosVisiveis(Long pastaId) {
        return documentoRepository.findByPastaIdAndStatusNotOrderByTituloAsc(pastaId, DocumentoEntity.STATUS_EXPIRADO)
                .stream()
                .map(this::toResponseComVersaoAtual)
                .toList();
    }

    private DocumentoResponse toResponseComVersaoAtual(DocumentoEntity documento) {
        DocumentoVersaoEntity versaoAtual = documentoVersaoRepository
                .findByDocumentoIdAndVersaoAtual(documento.getId(), DocumentoVersaoEntity.VERSAO_ATUAL_SIM)
                .orElseThrow(() -> new IllegalStateException("Documento sem versão atual: " + documento.getId()));
        ArquivoBinarioEntity arquivoAtual = arquivoBinarioRepository.findById(versaoAtual.getArquivoBinarioId())
                .orElseThrow(() -> new IllegalStateException(
                        "Arquivo binário ausente para documento: " + documento.getId()));
        return documentoMapper.toResponse(documento, arquivoAtual);
    }

    private Pageable normalize(Pageable pageable) {
        if (pageable.getSort().isUnsorted()) {
            return PageRequest.of(
                    PaginationUtils.normalizePage(pageable.getPageNumber()),
                    PaginationUtils.normalizeSize(pageable.getPageSize()),
                    Sort.by("nome"));
        }
        return PageRequest.of(
                PaginationUtils.normalizePage(pageable.getPageNumber()),
                PaginationUtils.normalizeSize(pageable.getPageSize()),
                pageable.getSort());
    }
}
