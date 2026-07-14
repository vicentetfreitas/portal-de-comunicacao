package br.com.unimedceara.portalcomunicacao.organization.application.service;

import br.com.unimedceara.portalcomunicacao.organization.domain.model.SingularStatus;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.SingularEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.SingularRepository;
import br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto.CreateSingularRequest;
import br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto.SingularResponse;
import br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto.UpdateSingularRequest;
import br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto.UpdateSingularStatusRequest;
import br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.mapper.SingularMapper;
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
 * Casos de uso da singular organizacional (FT-SINGULAR).
 */
@Service
@ConditionalOnProperty(prefix = "application.persistence", name = "enabled", havingValue = "true", matchIfMissing = true)
public class SingularApplicationService {

    private final SingularRepository singularRepository;
    private final SingularDomainService singularDomainService;
    private final SingularMapper singularMapper;
    private final OrganizationAuthorizationService organizationAuthorizationService;

    public SingularApplicationService(
            SingularRepository singularRepository,
            SingularDomainService singularDomainService,
            SingularMapper singularMapper,
            OrganizationAuthorizationService organizationAuthorizationService) {
        this.singularRepository = singularRepository;
        this.singularDomainService = singularDomainService;
        this.singularMapper = singularMapper;
        this.organizationAuthorizationService = organizationAuthorizationService;
    }

    @Transactional
    public SingularResponse create(CreateSingularRequest request, long colaboradorId) {
        organizationAuthorizationService.ensureOrganizationAdministrator(colaboradorId);

        singularDomainService.validateActiveFederacao(request.federacaoId());
        singularDomainService.validateUniqueAcronym(request.acronym(), null);
        singularDomainService.validateUniqueCodigoUnimed(request.codigoUnimed(), null);

        SingularEntity singular = new SingularEntity();
        singular.setFederacaoId(request.federacaoId());
        singular.setNome(request.name().trim());
        singular.setSigla(request.acronym().trim());
        singular.setCodigoUnimed(request.codigoUnimed().trim());
        singular.setAtivo(SingularStatus.ACTIVE.toFlag());
        singular.setDataCadastro(Instant.now());

        return singularMapper.toResponse(singularRepository.save(singular));
    }

    @Transactional(readOnly = true)
    public SingularResponse findById(Long id) {
        return singularMapper.toResponse(singularDomainService.loadSingularOrThrow(id));
    }

    @Transactional(readOnly = true)
    public PageResponse<SingularResponse> list(
            SingularStatus status,
            Long federacaoId,
            String name,
            String acronym,
            String codigoUnimed,
            Pageable pageable) {
        String ativoFlag = status == null ? null : status.toFlag();
        Pageable normalizedPageable = remapSort(pageable);
        Page<SingularEntity> page = singularRepository.findByFilters(
                federacaoId, ativoFlag, name, acronym, codigoUnimed, normalizedPageable);

        List<SingularResponse> content = page.getContent().stream().map(singularMapper::toResponse).toList();
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
    public SingularResponse update(Long id, UpdateSingularRequest request, long colaboradorId) {
        organizationAuthorizationService.ensureOrganizationAdministrator(colaboradorId);

        SingularEntity singular = singularDomainService.loadSingularOrThrow(id);
        singularDomainService.validateFederacaoActiveForUpdate(singular);
        singularDomainService.validateUniqueAcronym(request.acronym(), id);
        singularDomainService.validateUniqueCodigoUnimed(request.codigoUnimed(), id);

        singular.setNome(request.name().trim());
        singular.setSigla(request.acronym().trim());
        singular.setCodigoUnimed(request.codigoUnimed().trim());
        singular.setDataAtualizacao(Instant.now());

        return singularMapper.toResponse(singularRepository.save(singular));
    }

    @Transactional
    public SingularResponse updateStatus(Long id, UpdateSingularStatusRequest request, long colaboradorId) {
        organizationAuthorizationService.ensureOrganizationAdministrator(colaboradorId);

        SingularEntity singular = singularDomainService.loadSingularOrThrow(id);

        if (request.status() == SingularStatus.INACTIVE) {
            singularDomainService.validateDeactivation(singular);
            singular.setAtivo(SingularStatus.INACTIVE.toFlag());
        } else {
            singularDomainService.validateActiveFederacao(singular.getFederacaoId());
            singular.setAtivo(SingularStatus.ACTIVE.toFlag());
        }

        singular.setDataAtualizacao(Instant.now());
        return singularMapper.toResponse(singularRepository.save(singular));
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
