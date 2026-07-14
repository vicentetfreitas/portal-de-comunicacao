package br.com.unimedceara.portalcomunicacao.organization.application.service;

import br.com.unimedceara.portalcomunicacao.organization.domain.model.SingularStatus;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.FederacaoEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.SingularEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.AreaRepository;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.FederacaoRepository;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.SingularRepository;
import br.com.unimedceara.portalcomunicacao.shared.exception.BusinessException;
import br.com.unimedceara.portalcomunicacao.shared.exception.ResourceNotFoundException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * Validações de domínio da singular organizacional (RN-SINGULAR-001 a RN-SINGULAR-007).
 */
@Service
@ConditionalOnProperty(prefix = "application.persistence", name = "enabled", havingValue = "true", matchIfMissing = true)
public class SingularDomainService {

    private static final String BUSINESS_RULE_CODE = "BUSINESS_RULE_VIOLATION";

    private final SingularRepository singularRepository;
    private final FederacaoRepository federacaoRepository;
    private final AreaRepository areaRepository;

    public SingularDomainService(
            SingularRepository singularRepository,
            FederacaoRepository federacaoRepository,
            AreaRepository areaRepository) {
        this.singularRepository = singularRepository;
        this.federacaoRepository = federacaoRepository;
        this.areaRepository = areaRepository;
    }

    public void validateActiveFederacao(Long federacaoId) {
        FederacaoEntity federacao = federacaoRepository.findById(federacaoId)
                .orElseThrow(() -> new BusinessException(BUSINESS_RULE_CODE, "Federação inexistente"));
        if (!federacao.isAtivo()) {
            throw new BusinessException(BUSINESS_RULE_CODE, "Federação inativa");
        }
    }

    public void validateUniqueAcronym(String acronym, Long excludeId) {
        boolean exists = excludeId == null
                ? singularRepository.existsBySiglaIgnoreCase(acronym)
                : singularRepository.existsBySiglaIgnoreCaseAndIdNot(acronym, excludeId);
        if (exists) {
            throw new BusinessException(BUSINESS_RULE_CODE, "Já existe singular com esta sigla");
        }
    }

    public void validateUniqueCodigoUnimed(String codigoUnimed, Long excludeId) {
        boolean exists = excludeId == null
                ? singularRepository.existsByCodigoUnimedIgnoreCase(codigoUnimed)
                : singularRepository.existsByCodigoUnimedIgnoreCaseAndIdNot(codigoUnimed, excludeId);
        if (exists) {
            throw new BusinessException(BUSINESS_RULE_CODE, "Já existe singular com este código Unimed");
        }
    }

    public SingularEntity loadSingularOrThrow(Long id) {
        return singularRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Singular não encontrada"));
    }

    public void validateFederacaoActiveForUpdate(SingularEntity singular) {
        validateActiveFederacao(singular.getFederacaoId());
    }

    public void validateDeactivation(SingularEntity singular) {
        if (areaRepository.existsBySingularIdAndAtivo(singular.getId(), SingularStatus.ACTIVE.toFlag())) {
            throw new BusinessException(BUSINESS_RULE_CODE, "Singular possui áreas ativas vinculadas");
        }
    }
}
