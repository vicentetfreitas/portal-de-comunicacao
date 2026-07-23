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
 * Validações de domínio do colaborador.
 */
@Service
@ConditionalOnProperty(prefix = "application.persistence", name = "enabled", havingValue = "true", matchIfMissing = true)
public class ColaboradorDomainService {

    private static final String BUSINESS_RULE_CODE = "BUSINESS_RULE_VIOLATION";

    private final SingularRepository singularRepository;
    private final AreaRepository areaRepository;
    private final EquipeRepository equipeRepository;
    private final ColaboradorRepository colaboradorRepository;

    public ColaboradorDomainService(
            SingularRepository singularRepository,
            AreaRepository areaRepository,
            EquipeRepository equipeRepository,
            ColaboradorRepository colaboradorRepository) {
        this.singularRepository = singularRepository;
        this.areaRepository = areaRepository;
        this.equipeRepository = equipeRepository;
        this.colaboradorRepository = colaboradorRepository;
    }

    public void validateUniqueEmail(String email, Long excludeId) {
        boolean exists = excludeId == null
                ? colaboradorRepository.existsByEmailIgnoreCase(email)
                : colaboradorRepository.existsByEmailIgnoreCaseAndIdNot(email, excludeId);
        if (exists) {
            throw new BusinessException(BUSINESS_RULE_CODE, "Já existe colaborador com este e-mail");
        }
    }

    public void validateUniqueZimbraId(String zimbraId, Long excludeId) {
        if (zimbraId == null || zimbraId.isBlank()) {
            throw new BusinessException(BUSINESS_RULE_CODE, "Identificador Zimbra é obrigatório");
        }
        boolean exists = excludeId == null
                ? colaboradorRepository.existsByZimbraId(zimbraId)
                : colaboradorRepository.existsByZimbraIdAndIdNot(zimbraId, excludeId);
        if (exists) {
            throw new BusinessException(BUSINESS_RULE_CODE, "Já existe colaborador com este identificador Zimbra");
        }
    }

    public void validateManager(Long managerId, Long excludeId) {
        if (managerId == null) {
            return;
        }
        if (excludeId != null && excludeId.equals(managerId)) {
            throw new BusinessException(BUSINESS_RULE_CODE, "Colaborador não pode ser gestor de si mesmo");
        }
        ColaboradorEntity manager = colaboradorRepository.findById(managerId)
                .orElseThrow(() -> new BusinessException(BUSINESS_RULE_CODE, "Gestor inexistente"));
        if (!manager.isAtivo()) {
            throw new BusinessException(BUSINESS_RULE_CODE, "Gestor inativo");
        }
    }

    public OrganizationalContext resolveOrganizationalLinks(Long singularId, Long areaId, Long teamId) {
        Long resolvedSingularId = singularId;
        Long resolvedAreaId = areaId;
        Long resolvedTeamId = teamId;

        if (teamId != null) {
            EquipeEntity equipe = equipeRepository.findById(teamId)
                    .orElseThrow(() -> new BusinessException(BUSINESS_RULE_CODE, "Equipe inexistente"));
            if (!equipe.isAtivo()) {
                throw new BusinessException(BUSINESS_RULE_CODE, "Equipe inativa");
            }
            if (areaId != null && !areaId.equals(equipe.getAreaId())) {
                throw new BusinessException(BUSINESS_RULE_CODE, "Equipe não pertence à área informada");
            }
            resolvedAreaId = equipe.getAreaId();
            resolvedTeamId = equipe.getId();
        }

        if (resolvedAreaId != null) {
            AreaEntity area = areaRepository.findById(resolvedAreaId)
                    .orElseThrow(() -> new BusinessException(BUSINESS_RULE_CODE, "Área inexistente"));
            if (!area.isAtivo()) {
                throw new BusinessException(BUSINESS_RULE_CODE, "Área inativa");
            }
            if (resolvedSingularId != null && !resolvedSingularId.equals(area.getSingularId())) {
                throw new BusinessException(BUSINESS_RULE_CODE, "Área não pertence à singular informada");
            }
            resolvedSingularId = area.getSingularId();
        }

        if (resolvedSingularId != null) {
            SingularEntity singular = singularRepository.findById(resolvedSingularId)
                    .orElseThrow(() -> new BusinessException(BUSINESS_RULE_CODE, "Singular inexistente"));
            if (!singular.isAtivo()) {
                throw new BusinessException(BUSINESS_RULE_CODE, "Singular inativa");
            }
        }

        return new OrganizationalContext(resolvedSingularId, resolvedAreaId, resolvedTeamId);
    }

    public void validateDeactivation(ColaboradorEntity colaborador) {
        if (colaboradorRepository.existsByGestorIdAndAtivo(colaborador.getId(), ColaboradorStatus.ACTIVE.toFlag())) {
            throw new BusinessException(BUSINESS_RULE_CODE, "Colaborador possui subordinados ativos");
        }
    }

    public ColaboradorEntity loadColaboradorOrThrow(Long id) {
        return colaboradorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Colaborador não encontrado"));
    }

    public record OrganizationalContext(Long singularId, Long areaId, Long teamId) {
    }
}
