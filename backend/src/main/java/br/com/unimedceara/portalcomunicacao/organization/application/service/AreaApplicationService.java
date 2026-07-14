package br.com.unimedceara.portalcomunicacao.organization.application.service;

import br.com.unimedceara.portalcomunicacao.organization.domain.model.AreaStatus;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.AreaEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.AreaRepository;
import br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto.AreaResponse;
import br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto.CreateAreaRequest;
import br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto.UpdateAreaRequest;
import br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto.UpdateAreaStatusRequest;
import br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.mapper.AreaMapper;
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
 * Casos de uso da área organizacional (FT-AREA).
 */
@Service
@ConditionalOnProperty(prefix = "application.persistence", name = "enabled", havingValue = "true", matchIfMissing = true)
public class AreaApplicationService {

    private final AreaRepository areaRepository;
    private final AreaDomainService areaDomainService;
    private final AreaMapper areaMapper;
    private final OrganizationAuthorizationService organizationAuthorizationService;

    public AreaApplicationService(
            AreaRepository areaRepository,
            AreaDomainService areaDomainService,
            AreaMapper areaMapper,
            OrganizationAuthorizationService organizationAuthorizationService) {
        this.areaRepository = areaRepository;
        this.areaDomainService = areaDomainService;
        this.areaMapper = areaMapper;
        this.organizationAuthorizationService = organizationAuthorizationService;
    }

    @Transactional
    public AreaResponse create(CreateAreaRequest request, long colaboradorId) {
        organizationAuthorizationService.ensureOrganizationAdministrator(colaboradorId);

        areaDomainService.validateActiveSingular(request.singularId());
        areaDomainService.validateUniqueName(request.singularId(), request.name(), null);
        areaDomainService.validateParentArea(request.singularId(), request.parentAreaId());
        areaDomainService.validateManager(request.managerId());

        AreaEntity area = new AreaEntity();
        area.setSingularId(request.singularId());
        area.setParentAreaId(request.parentAreaId());
        area.setNome(request.name().trim());
        area.setSigla(request.acronym());
        area.setDescricao(request.description());
        area.setGestorId(request.managerId());
        area.setAtivo(AreaStatus.ACTIVE.toFlag());
        area.setDataCadastro(Instant.now());

        return areaMapper.toResponse(areaRepository.save(area));
    }

    @Transactional(readOnly = true)
    public AreaResponse findById(Long id) {
        return areaMapper.toResponse(areaDomainService.loadAreaOrThrow(id));
    }

    @Transactional(readOnly = true)
    public PageResponse<AreaResponse> list(
            AreaStatus status, Long singularId, String name, String acronym, Pageable pageable) {
        String ativoFlag = status == null ? null : status.toFlag();
        Pageable normalizedPageable = remapSort(pageable);
        Page<AreaEntity> page = areaRepository.findByFilters(singularId, ativoFlag, name, acronym, normalizedPageable);

        List<AreaResponse> content = page.getContent().stream().map(areaMapper::toResponse).toList();
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
    public AreaResponse update(Long id, UpdateAreaRequest request, long colaboradorId) {
        organizationAuthorizationService.ensureOrganizationAdministrator(colaboradorId);

        AreaEntity area = areaDomainService.loadAreaOrThrow(id);
        areaDomainService.validateSingularActiveForUpdate(area);
        areaDomainService.validateUniqueName(area.getSingularId(), request.name(), id);
        areaDomainService.validateParentArea(area.getSingularId(), request.parentAreaId());
        areaDomainService.validateHierarchyCycle(id, request.parentAreaId());
        areaDomainService.validateManager(request.managerId());

        area.setParentAreaId(request.parentAreaId());
        area.setNome(request.name().trim());
        area.setSigla(request.acronym());
        area.setDescricao(request.description());
        area.setGestorId(request.managerId());
        area.setDataAtualizacao(Instant.now());

        return areaMapper.toResponse(areaRepository.save(area));
    }

    @Transactional
    public AreaResponse updateStatus(Long id, UpdateAreaStatusRequest request, long colaboradorId) {
        organizationAuthorizationService.ensureOrganizationAdministrator(colaboradorId);

        AreaEntity area = areaDomainService.loadAreaOrThrow(id);

        if (request.status() == AreaStatus.INACTIVE) {
            areaDomainService.validateDeactivation(area);
            area.setAtivo(AreaStatus.INACTIVE.toFlag());
        } else {
            areaDomainService.validateActiveSingular(area.getSingularId());
            area.setAtivo(AreaStatus.ACTIVE.toFlag());
        }

        area.setDataAtualizacao(Instant.now());
        return areaMapper.toResponse(areaRepository.save(area));
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
            case "acronym" -> "sigla";
            case "createdAt" -> "dataCadastro";
            case "updatedAt" -> "dataAtualizacao";
            case "status" -> "ativo";
            default -> property;
        };
    }
}
