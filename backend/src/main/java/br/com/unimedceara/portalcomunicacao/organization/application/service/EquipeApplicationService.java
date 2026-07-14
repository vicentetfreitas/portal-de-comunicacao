package br.com.unimedceara.portalcomunicacao.organization.application.service;

import br.com.unimedceara.portalcomunicacao.organization.domain.model.EquipeStatus;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.EquipeEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.EquipeRepository;
import br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto.CreateEquipeRequest;
import br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto.EquipeResponse;
import br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto.UpdateEquipeRequest;
import br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto.UpdateEquipeStatusRequest;
import br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.mapper.EquipeMapper;
import br.com.unimedceara.portalcomunicacao.shared.dto.PageResponse;
import br.com.unimedceara.portalcomunicacao.shared.util.PaginationUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

/**
 * Casos de uso da equipe organizacional (FT-EQUIPE).
 */
@Service
@ConditionalOnProperty(prefix = "application.persistence", name = "enabled", havingValue = "true", matchIfMissing = true)
public class EquipeApplicationService {

    private final EquipeRepository equipeRepository;
    private final EquipeDomainService equipeDomainService;
    private final EquipeMapper equipeMapper;
    private final OrganizationAuthorizationService organizationAuthorizationService;

    public EquipeApplicationService(
            EquipeRepository equipeRepository,
            EquipeDomainService equipeDomainService,
            EquipeMapper equipeMapper,
            OrganizationAuthorizationService organizationAuthorizationService) {
        this.equipeRepository = equipeRepository;
        this.equipeDomainService = equipeDomainService;
        this.equipeMapper = equipeMapper;
        this.organizationAuthorizationService = organizationAuthorizationService;
    }

    @Transactional
    public EquipeResponse create(CreateEquipeRequest request, long colaboradorId) {
        organizationAuthorizationService.ensureOrganizationAdministrator(colaboradorId);
        equipeDomainService.validateActiveArea(request.areaId());
        equipeDomainService.validateUniqueName(request.areaId(), request.name(), null);
        equipeDomainService.validateLeader(request.leaderId());

        EquipeEntity equipe = new EquipeEntity();
        equipe.setAreaId(request.areaId());
        equipe.setNome(request.name().trim());
        equipe.setDescricao(request.description());
        equipe.setLiderId(request.leaderId());
        equipe.setAtivo(EquipeStatus.ACTIVE.toFlag());
        equipe.setDataCadastro(Instant.now());

        return equipeMapper.toResponse(equipeRepository.save(equipe));
    }

    @Transactional(readOnly = true)
    public EquipeResponse findById(Long id) {
        return equipeMapper.toResponse(equipeDomainService.loadEquipeOrThrow(id));
    }

    @Transactional(readOnly = true)
    public PageResponse<EquipeResponse> list(EquipeStatus status, Long areaId, String name, Pageable pageable) {
        String ativoFlag = status == null ? null : status.toFlag();
        Pageable normalizedPageable = remapSort(pageable);
        Page<EquipeEntity> page = equipeRepository.findByFilters(areaId, ativoFlag, name, normalizedPageable);
        List<EquipeResponse> content = page.getContent().stream().map(equipeMapper::toResponse).toList();
        return PageResponse.of(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast());
    }

    @Transactional
    public EquipeResponse update(Long id, UpdateEquipeRequest request, long colaboradorId) {
        organizationAuthorizationService.ensureOrganizationAdministrator(colaboradorId);
        EquipeEntity equipe = equipeDomainService.loadEquipeOrThrow(id);
        equipeDomainService.validateAreaActiveForUpdate(equipe);
        equipeDomainService.validateUniqueName(equipe.getAreaId(), request.name(), id);
        equipeDomainService.validateLeader(request.leaderId());

        equipe.setNome(request.name().trim());
        equipe.setDescricao(request.description());
        equipe.setLiderId(request.leaderId());
        equipe.setDataAtualizacao(Instant.now());

        return equipeMapper.toResponse(equipeRepository.save(equipe));
    }

    @Transactional
    public EquipeResponse updateStatus(Long id, UpdateEquipeStatusRequest request, long colaboradorId) {
        organizationAuthorizationService.ensureOrganizationAdministrator(colaboradorId);
        EquipeEntity equipe = equipeDomainService.loadEquipeOrThrow(id);

        if (request.status() == EquipeStatus.INACTIVE) {
            equipeDomainService.validateDeactivation(equipe);
            equipe.setAtivo(EquipeStatus.INACTIVE.toFlag());
        } else {
            equipeDomainService.validateAreaActiveForUpdate(equipe);
            equipe.setAtivo(EquipeStatus.ACTIVE.toFlag());
        }

        equipe.setDataAtualizacao(Instant.now());
        return equipeMapper.toResponse(equipeRepository.save(equipe));
    }

    private Pageable remapSort(Pageable pageable) {
        if (pageable.getSort().isUnsorted()) {
            return PageRequest.of(
                    PaginationUtils.normalizePage(pageable.getPageNumber()),
                    PaginationUtils.normalizeSize(pageable.getPageSize()));
        }
        List<Sort.Order> orders = pageable.getSort().stream()
                .map(order -> new Sort.Order(order.getDirection(), mapSortProperty(order.getProperty())))
                .toList();
        return PageRequest.of(
                PaginationUtils.normalizePage(pageable.getPageNumber()),
                PaginationUtils.normalizeSize(pageable.getPageSize()),
                Sort.by(orders));
    }

    private String mapSortProperty(String property) {
        return switch (property) {
            case "name" -> "nome";
            case "createdAt" -> "dataCadastro";
            case "updatedAt" -> "dataAtualizacao";
            case "status" -> "ativo";
            default -> property;
        };
    }
}
