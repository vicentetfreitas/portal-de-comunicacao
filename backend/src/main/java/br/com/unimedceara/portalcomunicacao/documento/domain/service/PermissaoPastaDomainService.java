package br.com.unimedceara.portalcomunicacao.documento.domain.service;

import br.com.unimedceara.portalcomunicacao.accesscontrol.domain.model.JwtAuthenticatedPrincipal;
import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.repository.PermissaoPastaRepository;
import br.com.unimedceara.portalcomunicacao.shared.exception.ForbiddenException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * Regra de autorização documental (RF-DOCUMENTO-003, BR-012/BR-018/BR-020): acesso exige
 * {@code PERMISSAO_PASTA} compatível com algum nível do Contexto Ativo do colaborador.
 */
@Service
@ConditionalOnProperty(prefix = "application.persistence", name = "enabled", havingValue = "true", matchIfMissing = true)
public class PermissaoPastaDomainService {

    private static final String MENSAGEM_SEM_PERMISSAO = "Colaborador não possui permissão de acesso a este recurso.";

    private final PermissaoPastaRepository permissaoPastaRepository;

    public PermissaoPastaDomainService(PermissaoPastaRepository permissaoPastaRepository) {
        this.permissaoPastaRepository = permissaoPastaRepository;
    }

    /**
     * Lança {@link ForbiddenException} (403) quando não há grant compatível — nunca oculta
     * o recurso como 404 (RF-DOCUMENTO-003).
     */
    public void ensureAccess(Long pastaId, String tipoAcesso, JwtAuthenticatedPrincipal principal) {
        boolean hasAccess = permissaoPastaRepository.existsGrant(
                pastaId,
                tipoAcesso,
                principal.federationId(),
                principal.singularId(),
                principal.areaId(),
                principal.teamId());

        if (!hasAccess) {
            throw new ForbiddenException(MENSAGEM_SEM_PERMISSAO);
        }
    }
}
