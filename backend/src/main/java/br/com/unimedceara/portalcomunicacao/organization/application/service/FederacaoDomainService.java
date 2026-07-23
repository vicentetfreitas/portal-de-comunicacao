package br.com.unimedceara.portalcomunicacao.organization.application.service;

import br.com.unimedceara.portalcomunicacao.organization.domain.model.FederacaoStatus;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.FederacaoEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.FederacaoRepository;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.SingularRepository;
import br.com.unimedceara.portalcomunicacao.shared.exception.BusinessException;
import br.com.unimedceara.portalcomunicacao.shared.exception.ResourceNotFoundException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * Validações de domínio da federação organizacional.
 */
@Service
@ConditionalOnProperty(prefix = "application.persistence", name = "enabled", havingValue = "true", matchIfMissing = true)
public class FederacaoDomainService {

    private static final String BUSINESS_RULE_CODE = "BUSINESS_RULE_VIOLATION";

    private final FederacaoRepository federacaoRepository;
    private final SingularRepository singularRepository;

    public FederacaoDomainService(FederacaoRepository federacaoRepository, SingularRepository singularRepository) {
        this.federacaoRepository = federacaoRepository;
        this.singularRepository = singularRepository;
    }

    public void validateUniqueAcronym(String acronym, Long excludeId) {
        boolean exists = excludeId == null
                ? federacaoRepository.existsBySiglaIgnoreCase(acronym)
                : federacaoRepository.existsBySiglaIgnoreCaseAndIdNot(acronym, excludeId);
        if (exists) {
            throw new BusinessException(BUSINESS_RULE_CODE, "Já existe federação com esta sigla");
        }
    }

    public void validateUniqueUnimedCode(Integer unimedCode, Long excludeId) {
        boolean exists = excludeId == null
                ? federacaoRepository.existsByCodigoUnimed(unimedCode)
                : federacaoRepository.existsByCodigoUnimedAndIdNot(unimedCode, excludeId);
        if (exists) {
            throw new BusinessException(BUSINESS_RULE_CODE, "Já existe federação com este código Unimed");
        }
    }

    public void validateUniqueAnsRegistration(String ansRegistration, Long excludeId) {
        boolean exists = excludeId == null
                ? federacaoRepository.existsByRegistroAns(ansRegistration)
                : federacaoRepository.existsByRegistroAnsAndIdNot(ansRegistration, excludeId);
        if (exists) {
            throw new BusinessException(BUSINESS_RULE_CODE, "Já existe federação com este registro ANS");
        }
    }

    public void validateDeactivation(FederacaoEntity federacao) {
        if (singularRepository.existsByFederacaoIdAndAtivo(federacao.getId(), FederacaoStatus.ACTIVE.toFlag())) {
            throw new BusinessException(BUSINESS_RULE_CODE, "Federação possui singulares ativas vinculadas");
        }
    }

    public FederacaoEntity loadFederacaoOrThrow(Long id) {
        return federacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Federação não encontrada"));
    }

    public FederacaoEntity loadActiveFederacaoOrThrow(Long id) {
        return federacaoRepository
                .findByIdAndAtivo(id, FederacaoStatus.ACTIVE.toFlag())
                .orElseThrow(() -> new BusinessException(BUSINESS_RULE_CODE, "Federação inexistente ou inativa"));
    }
}
