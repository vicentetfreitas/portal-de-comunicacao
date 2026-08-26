package br.com.unimedceara.portalcomunicacao.documento.application.service;

import br.com.unimedceara.portalcomunicacao.accesscontrol.domain.model.JwtAuthenticatedPrincipal;
import br.com.unimedceara.portalcomunicacao.documento.application.port.ObjectStorageClient;
import br.com.unimedceara.portalcomunicacao.documento.domain.service.PermissaoPastaDomainService;
import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.entity.ArquivoBinarioEntity;
import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.entity.DocumentoEntity;
import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.entity.DocumentoVersaoEntity;
import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.entity.PermissaoPastaEntity;
import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.repository.ArquivoBinarioRepository;
import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.repository.DocumentoRepository;
import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.repository.DocumentoVersaoRepository;
import br.com.unimedceara.portalcomunicacao.shared.exception.ResourceNotFoundException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso de download de documento (RF-DOCUMENTO-002, RF-DOCUMENTO-003, RF-DOCUMENTO-004
 * — UC-DOCUMENTO-002/003/004).
 */
@Service
@ConditionalOnProperty(prefix = "application.persistence", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DocumentoApplicationService {

    private static final String MENSAGEM_NAO_ENCONTRADO = "Documento não encontrado.";

    private final DocumentoRepository documentoRepository;
    private final DocumentoVersaoRepository documentoVersaoRepository;
    private final ArquivoBinarioRepository arquivoBinarioRepository;
    private final PermissaoPastaDomainService permissaoPastaDomainService;
    private final ObjectStorageClient objectStorageClient;

    public DocumentoApplicationService(
            DocumentoRepository documentoRepository,
            DocumentoVersaoRepository documentoVersaoRepository,
            ArquivoBinarioRepository arquivoBinarioRepository,
            PermissaoPastaDomainService permissaoPastaDomainService,
            ObjectStorageClient objectStorageClient) {
        this.documentoRepository = documentoRepository;
        this.documentoVersaoRepository = documentoVersaoRepository;
        this.arquivoBinarioRepository = arquivoBinarioRepository;
        this.permissaoPastaDomainService = permissaoPastaDomainService;
        this.objectStorageClient = objectStorageClient;
    }

    @Transactional(readOnly = true)
    public DocumentoDownload download(Long documentoId, JwtAuthenticatedPrincipal principal) {
        DocumentoEntity documento = documentoRepository.findById(documentoId)
                .orElseThrow(() -> new ResourceNotFoundException(MENSAGEM_NAO_ENCONTRADO));

        // RF-DOCUMENTO-004: expirado é tratado como inexistente, nunca distinguível por resposta.
        if (documento.isExpirado()) {
            throw new ResourceNotFoundException(MENSAGEM_NAO_ENCONTRADO);
        }

        // RF-DOCUMENTO-003: 403 explícito, nunca 404 disfarçado — checado depois do 404 real.
        permissaoPastaDomainService.ensureAccess(
                documento.getPastaId(), PermissaoPastaEntity.ACESSO_DOWNLOAD, principal);

        DocumentoVersaoEntity versaoAtual = documentoVersaoRepository
                .findByDocumentoIdAndVersaoAtual(documentoId, DocumentoVersaoEntity.VERSAO_ATUAL_SIM)
                .orElseThrow(() -> new IllegalStateException("Documento sem versão atual: " + documentoId));
        ArquivoBinarioEntity arquivoAtual = arquivoBinarioRepository.findById(versaoAtual.getArquivoBinarioId())
                .orElseThrow(() -> new IllegalStateException("Arquivo binário ausente: " + documentoId));

        return new DocumentoDownload(
                arquivoAtual.getNomeArquivo(),
                arquivoAtual.getTipoMime(),
                arquivoAtual.getTamanhoBytes(),
                objectStorageClient.download(arquivoAtual.getUrlArquivo()));
    }
}
