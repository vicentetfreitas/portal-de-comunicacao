package br.com.unimedceara.portalcomunicacao.accesscontrol.application.service;

import br.com.unimedceara.portalcomunicacao.accesscontrol.domain.model.ColaboradorStatus;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.ColaboradorEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository.ColaboradorRepository;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.AreaEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.EquipeEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.SingularEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.AreaRepository;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.EquipeRepository;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.SingularRepository;
import br.com.unimedceara.portalcomunicacao.shared.exception.BusinessException;
import br.com.unimedceara.portalcomunicacao.shared.exception.ResourceNotFoundException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * Validações de domínio do colaborador (RN-COLABORADOR-001 a RN-COLABORADOR-009).
 */
@Service
@ConditionalOnProperty(prefix = "application.persistence", name = "enabled", havingValue = "true", matchIfMissing = true)
public class ColaboradorDomainService {

    private static final String BUSINESS_RULE_CODE = "BUSINESS_RULE_VIOLATION";

    private final ColaboradorRepository colaboradorRepository;
    private final SingularRepository singularRepository;
    private final AreaRepository areaRepository;
    private final EquipeRepository equipeRepository;

    public ColaboradorDomainService(
            ColaboradorRepository colaboradorRepository,
            SingularRepository singularRepository,
            AreaRepository areaRepository,
            EquipeRepository equipeRepository) {
        this.colaboradorRepository = colaboradorRepository;
        this.singularRepository = singularRepository;
        this.areaRepository = areaRepository;
        this.equipeRepository = equipeRepository;
    }

    public void validateUniqueEmail(String email, Long excludeId) {
        boolean exists = excludeId == null
                ? colaboradorRepository.existsByEmailIgnoreCase(email)
                : colaboradorRepository.existsByEmailIgnoreCaseAndIdNot(email, excludeId);
        if (exists) {
            throw new BusinessException(BUSINESS_RULE_CODE, "Já existe colaborador com este e-mail");
        }
    }

    public void validateUniqueCpf(String cpf, Long excludeId) {
        if (cpf == null || cpf.isBlank()) {
            return;
        }
        boolean exists = excludeId == null
                ? colaboradorRepository.existsByCpf(cpf)
                : colaboradorRepository.existsByCpfAndIdNot(cpf, excludeId);
        if (exists) {
            throw new BusinessException(BUSINESS_RULE_CODE, "Já existe colaborador com este CPF");
        }
    }

    public void validateOrganizationalContext(Long singularId, Long areaId, Long teamId) {
        if (teamId != null && areaId == null) {
            throw new BusinessException(BUSINESS_RULE_CODE, "Área obrigatória quando equipe informada");
        }
        if (areaId != null && singularId == null) {
            throw new BusinessException(BUSINESS_RULE_CODE, "Singular obrigatória quando área informada");
        }

        if (singularId != null) {
            SingularEntity singular = singularRepository.findById(singularId)
                    .orElseThrow(() -> new BusinessException(BUSINESS_RULE_CODE, "Singular inexistente"));
            if (!singular.isAtivo()) {
                throw new BusinessException(BUSINESS_RULE_CODE, "Singular inativa");
            }
        }

        if (areaId != null) {
            AreaEntity area = areaRepository.findById(areaId)
                    .orElseThrow(() -> new BusinessException(BUSINESS_RULE_CODE, "Área inexistente"));
            if (!area.isAtivo()) {
                throw new BusinessException(BUSINESS_RULE_CODE, "Área inativa");
            }
            if (singularId != null && !singularId.equals(area.getSingularId())) {
                throw new BusinessException(BUSINESS_RULE_CODE, "Área pertence a outra singular");
            }
        }

        if (teamId != null) {
            EquipeEntity equipe = equipeRepository.findById(teamId)
                    .orElseThrow(() -> new BusinessException(BUSINESS_RULE_CODE, "Equipe inexistente"));
            if (!equipe.isAtivo()) {
                throw new BusinessException(BUSINESS_RULE_CODE, "Equipe inativa");
            }
            if (!areaId.equals(equipe.getAreaId())) {
                throw new BusinessException(BUSINESS_RULE_CODE, "Equipe pertence a outra área");
            }
        }
    }

    public void validateManager(Long managerId, Long colaboradorId) {
        if (managerId == null) {
            return;
        }
        if (colaboradorId != null && colaboradorId.equals(managerId)) {
            throw new BusinessException(BUSINESS_RULE_CODE, "Colaborador não pode ser gestor de si mesmo");
        }

        ColaboradorEntity manager = colaboradorRepository.findById(managerId)
                .orElseThrow(() -> new BusinessException(BUSINESS_RULE_CODE, "Gestor inexistente"));
        if (!manager.isAtivo()) {
            throw new BusinessException(BUSINESS_RULE_CODE, "Gestor inativo");
        }
    }

    public void validateDeactivation(ColaboradorEntity colaborador) {
        if (colaboradorRepository.existsByGestorIdAndAtivo(colaborador.getId(), ColaboradorStatus.ACTIVE.toFlag())) {
            throw new BusinessException(BUSINESS_RULE_CODE, "Colaborador possui subordinados ativos vinculados");
        }
    }

    public ColaboradorEntity loadColaboradorOrThrow(Long id) {
        return colaboradorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Colaborador não encontrado"));
    }

    public void validateOrganizationalContextForUpdate(ColaboradorEntity colaborador) {
        validateOrganizationalContext(colaborador.getSingularId(), colaborador.getAreaId(), colaborador.getEquipeId());
    }
}
