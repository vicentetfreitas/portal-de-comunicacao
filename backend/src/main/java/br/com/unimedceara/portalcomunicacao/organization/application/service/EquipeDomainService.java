package br.com.unimedceara.portalcomunicacao.organization.application.service;

import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.ColaboradorEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository.ColaboradorRepository;
import br.com.unimedceara.portalcomunicacao.organization.domain.model.AreaStatus;
import br.com.unimedceara.portalcomunicacao.organization.domain.model.EquipeStatus;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.AreaEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.EquipeEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.EquipeRepository;
import br.com.unimedceara.portalcomunicacao.shared.exception.BusinessException;
import br.com.unimedceara.portalcomunicacao.shared.exception.ResourceNotFoundException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * Validações de domínio da equipe organizacional (RN-EQUIPE-001 a RN-EQUIPE-007).
 */
@Service
@ConditionalOnProperty(prefix = "application.persistence", name = "enabled", havingValue = "true", matchIfMissing = true)
public class EquipeDomainService {

    private static final String BUSINESS_RULE_CODE = "BUSINESS_RULE_VIOLATION";

    private final AreaDomainService areaDomainService;
    private final EquipeRepository equipeRepository;
    private final ColaboradorRepository colaboradorRepository;

    public EquipeDomainService(
            AreaDomainService areaDomainService,
            EquipeRepository equipeRepository,
            ColaboradorRepository colaboradorRepository) {
        this.areaDomainService = areaDomainService;
        this.equipeRepository = equipeRepository;
        this.colaboradorRepository = colaboradorRepository;
    }

    public void validateActiveArea(Long areaId) {
        AreaEntity area = areaDomainService.loadAreaOrThrow(areaId);
        if (!area.isAtivo()) {
            throw new BusinessException(BUSINESS_RULE_CODE, "Área inativa");
        }
    }

    public void validateUniqueName(Long areaId, String name, Long excludeEquipeId) {
        boolean exists = excludeEquipeId == null
                ? equipeRepository.existsByAreaIdAndNomeIgnoreCaseAndAtivo(
                        areaId, name, EquipeStatus.ACTIVE.toFlag())
                : equipeRepository.existsByAreaIdAndNomeIgnoreCaseAndAtivoAndIdNot(
                        areaId, name, EquipeStatus.ACTIVE.toFlag(), excludeEquipeId);
        if (exists) {
            throw new BusinessException(BUSINESS_RULE_CODE, "Já existe equipe ativa com este nome na área");
        }
    }

    public void validateLeader(Long leaderId) {
        if (leaderId == null) {
            return;
        }

        ColaboradorEntity leader = colaboradorRepository.findById(leaderId)
                .orElseThrow(() -> new BusinessException(BUSINESS_RULE_CODE, "Líder inexistente"));
        if (!leader.isAtivo()) {
            throw new BusinessException(BUSINESS_RULE_CODE, "Líder inativo");
        }
    }

    public void validateDeactivation(EquipeEntity equipe) {
        if (colaboradorRepository.existsByEquipeIdAndAtivo(equipe.getId(), AreaStatus.ACTIVE.toFlag())) {
            throw new BusinessException(BUSINESS_RULE_CODE, "Equipe possui colaboradores ativos vinculados");
        }
    }

    public EquipeEntity loadEquipeOrThrow(Long id) {
        return equipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipe não encontrada"));
    }

    public void validateAreaActiveForUpdate(EquipeEntity equipe) {
        validateActiveArea(equipe.getAreaId());
    }
}
