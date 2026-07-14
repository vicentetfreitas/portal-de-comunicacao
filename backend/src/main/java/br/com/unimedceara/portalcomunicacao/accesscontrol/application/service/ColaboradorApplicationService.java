package br.com.unimedceara.portalcomunicacao.accesscontrol.application.service;

import br.com.unimedceara.portalcomunicacao.accesscontrol.domain.model.ColaboradorStatus;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.ColaboradorEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository.ColaboradorRepository;
import br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest.dto.ColaboradorResponse;
import br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest.dto.CreateColaboradorRequest;
import br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest.dto.UpdateColaboradorRequest;
import br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest.dto.UpdateColaboradorStatusRequest;
import br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest.mapper.ColaboradorMapper;
import br.com.unimedceara.portalcomunicacao.organization.application.service.OrganizationAuthorizationService;
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
 * Casos de uso administrativos do colaborador (FT-COLABORADOR).
 */
@Service
@ConditionalOnProperty(prefix = "application.persistence", name = "enabled", havingValue = "true", matchIfMissing = true)
public class ColaboradorApplicationService {

    private final ColaboradorRepository colaboradorRepository;
    private final ColaboradorDomainService colaboradorDomainService;
    private final ColaboradorMapper colaboradorMapper;
    private final OrganizationAuthorizationService organizationAuthorizationService;

    public ColaboradorApplicationService(
            ColaboradorRepository colaboradorRepository,
            ColaboradorDomainService colaboradorDomainService,
            ColaboradorMapper colaboradorMapper,
            OrganizationAuthorizationService organizationAuthorizationService) {
        this.colaboradorRepository = colaboradorRepository;
        this.colaboradorDomainService = colaboradorDomainService;
        this.colaboradorMapper = colaboradorMapper;
        this.organizationAuthorizationService = organizationAuthorizationService;
    }

    @Transactional
    public ColaboradorResponse create(CreateColaboradorRequest request, long colaboradorId) {
        organizationAuthorizationService.ensureOrganizationAdministrator(colaboradorId);

        String email = request.email().trim().toLowerCase();
        colaboradorDomainService.validateUniqueEmail(email, null);
        colaboradorDomainService.validateUniqueCpf(request.cpf(), null);
        colaboradorDomainService.validateOrganizationalContext(
                request.singularId(), request.areaId(), request.teamId());
        colaboradorDomainService.validateManager(request.managerId(), null);

        ColaboradorEntity colaborador = new ColaboradorEntity();
        colaborador.setFederacaoId(request.federationId());
        colaborador.setSingularId(request.singularId());
        colaborador.setAreaId(request.areaId());
        colaborador.setEquipeId(request.teamId());
        colaborador.setGestorId(request.managerId());
        colaborador.setNome(request.name().trim());
        colaborador.setEmail(email);
        colaborador.setCargo(request.jobTitle());
        colaborador.setCpf(request.cpf());
        colaborador.setZimbraId(request.zimbraId());
        colaborador.setBiografia(request.biography());
        colaborador.setAtivo(ColaboradorStatus.ACTIVE.toFlag());
        colaborador.setDataCadastro(Instant.now());

        return colaboradorMapper.toResponse(colaboradorRepository.save(colaborador));
    }

    @Transactional(readOnly = true)
    public ColaboradorResponse findById(Long id) {
        return colaboradorMapper.toResponse(colaboradorDomainService.loadColaboradorOrThrow(id));
    }

    @Transactional(readOnly = true)
    public PageResponse<ColaboradorResponse> list(
            ColaboradorStatus status,
            Long singularId,
            Long areaId,
            Long teamId,
            String name,
            String email,
            Pageable pageable) {
        String ativoFlag = status == null ? null : status.toFlag();
        Pageable normalizedPageable = remapSort(pageable);
        Page<ColaboradorEntity> page = colaboradorRepository.findByFilters(
                singularId, areaId, teamId, ativoFlag, name, email, normalizedPageable);

        List<ColaboradorResponse> content =
                page.getContent().stream().map(colaboradorMapper::toResponse).toList();
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
    public ColaboradorResponse update(Long id, UpdateColaboradorRequest request, long colaboradorId) {
        organizationAuthorizationService.ensureOrganizationAdministrator(colaboradorId);

        ColaboradorEntity colaborador = colaboradorDomainService.loadColaboradorOrThrow(id);
        colaboradorDomainService.validateUniqueCpf(request.cpf(), id);
        colaboradorDomainService.validateOrganizationalContext(
                request.singularId(), request.areaId(), request.teamId());
        colaboradorDomainService.validateManager(request.managerId(), id);

        colaborador.setSingularId(request.singularId());
        colaborador.setAreaId(request.areaId());
        colaborador.setEquipeId(request.teamId());
        colaborador.setGestorId(request.managerId());
        colaborador.setNome(request.name().trim());
        colaborador.setCargo(request.jobTitle());
        colaborador.setCpf(request.cpf());
        colaborador.setBiografia(request.biography());
        colaborador.setDataAtualizacao(Instant.now());

        return colaboradorMapper.toResponse(colaboradorRepository.save(colaborador));
    }

    @Transactional
    public ColaboradorResponse updateStatus(Long id, UpdateColaboradorStatusRequest request, long colaboradorId) {
        organizationAuthorizationService.ensureOrganizationAdministrator(colaboradorId);

        ColaboradorEntity colaborador = colaboradorDomainService.loadColaboradorOrThrow(id);

        if (request.status() == ColaboradorStatus.INACTIVE) {
            colaboradorDomainService.validateDeactivation(colaborador);
            colaborador.setAtivo(ColaboradorStatus.INACTIVE.toFlag());
        } else {
            colaboradorDomainService.validateOrganizationalContextForUpdate(colaborador);
            colaborador.setAtivo(ColaboradorStatus.ACTIVE.toFlag());
        }

        colaborador.setDataAtualizacao(Instant.now());
        return colaboradorMapper.toResponse(colaboradorRepository.save(colaborador));
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
            case "email" -> "email";
            case "createdAt" -> "dataCadastro";
            case "updatedAt" -> "dataAtualizacao";
            case "status" -> "ativo";
            default -> property;
        };
    }
}
