package br.com.unimedceara.portalcomunicacao.organization.application.service;

import br.com.unimedceara.portalcomunicacao.organization.domain.model.FederacaoStatus;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.FederacaoEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.FederacaoRepository;
import br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto.CreateFederacaoRequest;
import br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto.FederacaoResponse;
import br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto.UpdateFederacaoRequest;
import br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto.UpdateFederacaoStatusRequest;
import br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.mapper.FederacaoMapper;
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
 * Casos de uso da federação organizacional (FT-FEDERACAO).
 */
@Service
@ConditionalOnProperty(prefix = "application.persistence", name = "enabled", havingValue = "true", matchIfMissing = true)
public class FederacaoApplicationService {

    private final FederacaoRepository federacaoRepository;
    private final FederacaoDomainService federacaoDomainService;
    private final FederacaoMapper federacaoMapper;
    private final OrganizationAuthorizationService organizationAuthorizationService;

    public FederacaoApplicationService(
            FederacaoRepository federacaoRepository,
            FederacaoDomainService federacaoDomainService,
            FederacaoMapper federacaoMapper,
            OrganizationAuthorizationService organizationAuthorizationService) {
        this.federacaoRepository = federacaoRepository;
        this.federacaoDomainService = federacaoDomainService;
        this.federacaoMapper = federacaoMapper;
        this.organizationAuthorizationService = organizationAuthorizationService;
    }

    @Transactional
    public FederacaoResponse create(CreateFederacaoRequest request, long colaboradorId) {
        organizationAuthorizationService.ensureOrganizationAdministrator(colaboradorId);
        federacaoDomainService.validateUniqueAcronym(request.acronym(), null);
        federacaoDomainService.validateUniqueUnimedCode(request.unimedCode(), null);
        federacaoDomainService.validateUniqueAnsRegistration(request.ansRegistration(), null);

        FederacaoEntity federacao = new FederacaoEntity();
        federacao.setNome(request.name().trim());
        federacao.setSigla(request.acronym().trim());
        federacao.setCodigoUnimed(request.unimedCode());
        federacao.setRegistroAns(request.ansRegistration().trim());
        federacao.setUrlSite(trimToNull(request.websiteUrl()));
        federacao.setDescricao(request.description());
        federacao.setAtivo(FederacaoStatus.ACTIVE.toFlag());
        federacao.setDataCadastro(Instant.now());

        return federacaoMapper.toResponse(federacaoRepository.save(federacao));
    }

    @Transactional(readOnly = true)
    public FederacaoResponse findById(Long id) {
        return federacaoMapper.toResponse(federacaoDomainService.loadFederacaoOrThrow(id));
    }

    @Transactional(readOnly = true)
    public PageResponse<FederacaoResponse> list(
            FederacaoStatus status, String name, String acronym, Integer unimedCode, Pageable pageable) {
        String ativoFlag = status == null ? null : status.toFlag();
        Pageable normalizedPageable = remapSort(pageable);
        Page<FederacaoEntity> page =
                federacaoRepository.findByFilters(ativoFlag, name, acronym, unimedCode, normalizedPageable);
        List<FederacaoResponse> content = page.getContent().stream().map(federacaoMapper::toResponse).toList();
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
    public FederacaoResponse update(Long id, UpdateFederacaoRequest request, long colaboradorId) {
        organizationAuthorizationService.ensureOrganizationAdministrator(colaboradorId);
        FederacaoEntity federacao = federacaoDomainService.loadFederacaoOrThrow(id);
        federacaoDomainService.validateUniqueAcronym(request.acronym(), id);
        federacaoDomainService.validateUniqueUnimedCode(request.unimedCode(), id);
        federacaoDomainService.validateUniqueAnsRegistration(request.ansRegistration(), id);

        federacao.setNome(request.name().trim());
        federacao.setSigla(request.acronym().trim());
        federacao.setCodigoUnimed(request.unimedCode());
        federacao.setRegistroAns(request.ansRegistration().trim());
        federacao.setUrlSite(trimToNull(request.websiteUrl()));
        federacao.setDescricao(request.description());
        federacao.setDataAtualizacao(Instant.now());

        return federacaoMapper.toResponse(federacaoRepository.save(federacao));
    }

    @Transactional
    public FederacaoResponse updateStatus(Long id, UpdateFederacaoStatusRequest request, long colaboradorId) {
        organizationAuthorizationService.ensureOrganizationAdministrator(colaboradorId);
        FederacaoEntity federacao = federacaoDomainService.loadFederacaoOrThrow(id);

        if (request.status() == FederacaoStatus.INACTIVE) {
            federacaoDomainService.validateDeactivation(federacao);
            federacao.setAtivo(FederacaoStatus.INACTIVE.toFlag());
        } else {
            federacao.setAtivo(FederacaoStatus.ACTIVE.toFlag());
        }

        federacao.setDataAtualizacao(Instant.now());
        return federacaoMapper.toResponse(federacaoRepository.save(federacao));
    }

    private static String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
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
            case "unimedCode" -> "codigoUnimed";
            case "ansRegistration" -> "registroAns";
            case "createdAt" -> "dataCadastro";
            case "updatedAt" -> "dataAtualizacao";
            case "status" -> "ativo";
            default -> property;
        };
    }
}
