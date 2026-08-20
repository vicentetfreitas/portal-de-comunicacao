package br.com.unimedceara.portalcomunicacao.organization.application.service;

import br.com.unimedceara.portalcomunicacao.organization.domain.model.SingularStatus;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.SingularEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.AreaRepository;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.SingularRepository;
import br.com.unimedceara.portalcomunicacao.shared.exception.BusinessException;
import br.com.unimedceara.portalcomunicacao.shared.exception.ResourceNotFoundException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Regras de domínio da singular (unicidade, inativação).
 */
@Service
@ConditionalOnProperty(prefix = "application.persistence", name = "enabled", havingValue = "true", matchIfMissing = true)
public class SingularDomainService {

    private static final String BUSINESS_RULE_CODE = "SINGULAR_BUSINESS_RULE";

    private final SingularRepository singularRepository;
    private final AreaRepository areaRepository;

    public SingularDomainService(SingularRepository singularRepository, AreaRepository areaRepository) {
        this.singularRepository = singularRepository;
        this.areaRepository = areaRepository;
    }

    public void validateUniqueAcronym(String acronym, Long excludeId) {
        boolean exists = excludeId == null
                ? singularRepository.existsBySiglaIgnoreCase(acronym)
                : singularRepository.existsBySiglaIgnoreCaseAndIdNot(acronym, excludeId);
        if (exists) {
            throw new BusinessException(BUSINESS_RULE_CODE, "Sigla já cadastrada");
        }
    }

    public void validateUniqueUnimedCode(Integer unimedCode, Long excludeId) {
        boolean exists = excludeId == null
                ? singularRepository.existsByCodigoUnimed(unimedCode)
                : singularRepository.existsByCodigoUnimedAndIdNot(unimedCode, excludeId);
        if (exists) {
            throw new BusinessException(BUSINESS_RULE_CODE, "Código Unimed já cadastrado");
        }
    }

    public void validateDeactivation(SingularEntity singular) {
        if (areaRepository.existsBySingularIdAndAtivo(singular.getId(), SingularStatus.ACTIVE.toFlag())) {
            throw new BusinessException(BUSINESS_RULE_CODE, "Singular possui áreas ativas vinculadas");
        }
    }

    public SingularEntity loadSingularOrThrow(Long id) {
        return singularRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Singular não encontrada"));
    }

    /**
     * Resolve Singular ativa pelo domínio do e-mail autenticado (DEC-ORG-003, DH-PA-02).
     */
    public Optional<SingularEntity> findActiveByAuthenticatedEmail(String email) {
        String domain = emailDomain(email);
        if (domain == null) {
            return Optional.empty();
        }
        return singularRepository.findByDominioEmailIgnoreCaseAndAtivo(domain, SingularStatus.ACTIVE.toFlag());
    }

    static String emailDomain(String email) {
        if (email == null) {
            return null;
        }
        String normalized = email.trim().toLowerCase();
        int at = normalized.indexOf('@');
        if (at <= 0 || at != normalized.lastIndexOf('@') || at == normalized.length() - 1) {
            return null;
        }
        String domain = normalized.substring(at + 1).trim();
        return domain.isEmpty() ? null : domain;
    }
}
