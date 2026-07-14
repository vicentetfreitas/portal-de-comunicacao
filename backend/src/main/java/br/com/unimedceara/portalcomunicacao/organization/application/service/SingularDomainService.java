package br.com.unimedceara.portalcomunicacao.organization.application.service;

import br.com.unimedceara.portalcomunicacao.organization.domain.model.SingularStatus;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.SingularEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.AreaRepository;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.SingularRepository;
import br.com.unimedceara.portalcomunicacao.shared.exception.BusinessException;
import br.com.unimedceara.portalcomunicacao.shared.exception.ResourceNotFoundException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * Validações de domínio da singular organizacional.
 */
@Service
@ConditionalOnProperty(prefix = "application.persistence", name = "enabled", havingValue = "true", matchIfMissing = true)
public class SingularDomainService {

    private static final String BUSINESS_RULE_CODE = "BUSINESS_RULE_VIOLATION";

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
            throw new BusinessException(BUSINESS_RULE_CODE, "Já existe singular com esta sigla");
        }
    }

    public void validateUniqueUnimedCode(String unimedCode, Long excludeId) {
        boolean exists = excludeId == null
                ? singularRepository.existsByCodigoUnimedIgnoreCase(unimedCode)
                : singularRepository.existsByCodigoUnimedIgnoreCaseAndIdNot(unimedCode, excludeId);
        if (exists) {
            throw new BusinessException(BUSINESS_RULE_CODE, "Já existe singular com este código Unimed");
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
}
