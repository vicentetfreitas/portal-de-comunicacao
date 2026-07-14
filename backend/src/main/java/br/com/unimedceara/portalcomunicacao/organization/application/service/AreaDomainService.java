package br.com.unimedceara.portalcomunicacao.organization.application.service;

import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.ColaboradorEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository.ColaboradorRepository;
import br.com.unimedceara.portalcomunicacao.organization.domain.model.AreaStatus;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.AreaEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.SingularEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.AreaRepository;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.EquipeRepository;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.SingularRepository;
import br.com.unimedceara.portalcomunicacao.shared.exception.BusinessException;
import br.com.unimedceara.portalcomunicacao.shared.exception.ResourceNotFoundException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * Validações de domínio da área organizacional (RN-AREA-001 a RN-AREA-009).
 */
@Service
@ConditionalOnProperty(prefix = "application.persistence", name = "enabled", havingValue = "true", matchIfMissing = true)
public class AreaDomainService {

    private static final String BUSINESS_RULE_CODE = "BUSINESS_RULE_VIOLATION";

    private final SingularRepository singularRepository;
    private final AreaRepository areaRepository;
    private final EquipeRepository equipeRepository;
    private final ColaboradorRepository colaboradorRepository;

    public AreaDomainService(
            SingularRepository singularRepository,
            AreaRepository areaRepository,
            EquipeRepository equipeRepository,
            ColaboradorRepository colaboradorRepository) {
        this.singularRepository = singularRepository;
        this.areaRepository = areaRepository;
        this.equipeRepository = equipeRepository;
        this.colaboradorRepository = colaboradorRepository;
    }

    public void validateActiveSingular(Long singularId) {
        SingularEntity singular = singularRepository.findById(singularId)
                .orElseThrow(() -> new BusinessException(BUSINESS_RULE_CODE, "Singular inexistente"));
        if (!singular.isAtivo()) {
            throw new BusinessException(BUSINESS_RULE_CODE, "Singular inativa");
        }
    }

    public void validateUniqueName(Long singularId, String name, Long excludeAreaId) {
        boolean exists = excludeAreaId == null
                ? areaRepository.existsBySingularIdAndNomeIgnoreCaseAndAtivo(singularId, name, AreaStatus.ACTIVE.toFlag())
                : areaRepository.existsBySingularIdAndNomeIgnoreCaseAndAtivoAndIdNot(
                        singularId, name, AreaStatus.ACTIVE.toFlag(), excludeAreaId);
        if (exists) {
            throw new BusinessException(BUSINESS_RULE_CODE, "Já existe área ativa com este nome na singular");
        }
    }

    public void validateParentArea(Long singularId, Long parentAreaId) {
        if (parentAreaId == null) {
            return;
        }

        AreaEntity parent = areaRepository.findById(parentAreaId)
                .orElseThrow(() -> new BusinessException(BUSINESS_RULE_CODE, "Área pai inexistente"));
        if (!parent.isAtivo()) {
            throw new BusinessException(BUSINESS_RULE_CODE, "Área pai inativa");
        }
        if (!singularId.equals(parent.getSingularId())) {
            throw new BusinessException(BUSINESS_RULE_CODE, "Área pai pertence a outra singular");
        }
    }

    public void validateHierarchyCycle(Long areaId, Long parentAreaId) {
        if (parentAreaId == null) {
            return;
        }
        if (areaId != null && areaId.equals(parentAreaId)) {
            throw new BusinessException(BUSINESS_RULE_CODE, "Área não pode ser pai de si mesma");
        }

        Long currentParentId = parentAreaId;
        while (currentParentId != null) {
            if (areaId != null && areaId.equals(currentParentId)) {
                throw new BusinessException(BUSINESS_RULE_CODE, "Hierarquia de área formaria ciclo");
            }
            currentParentId = areaRepository.findById(currentParentId)
                    .map(AreaEntity::getParentAreaId)
                    .orElse(null);
        }
    }

    public void validateManager(Long managerId) {
        if (managerId == null) {
            return;
        }

        ColaboradorEntity manager = colaboradorRepository.findById(managerId)
                .orElseThrow(() -> new BusinessException(BUSINESS_RULE_CODE, "Gestor inexistente"));
        if (!manager.isAtivo()) {
            throw new BusinessException(BUSINESS_RULE_CODE, "Gestor inativo");
        }
    }

    public void validateDeactivation(AreaEntity area) {
        if (equipeRepository.existsByAreaIdAndAtivo(area.getId(), AreaStatus.ACTIVE.toFlag())) {
            throw new BusinessException(BUSINESS_RULE_CODE, "Área possui equipes ativas vinculadas");
        }
        if (areaRepository.existsByParentAreaIdAndAtivo(area.getId(), AreaStatus.ACTIVE.toFlag())) {
            throw new BusinessException(BUSINESS_RULE_CODE, "Área possui áreas filhas ativas vinculadas");
        }
    }

    public AreaEntity loadAreaOrThrow(Long id) {
        return areaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Área não encontrada"));
    }

    public void validateSingularActiveForUpdate(AreaEntity area) {
        validateActiveSingular(area.getSingularId());
    }
}
