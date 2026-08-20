package br.com.unimedceara.portalcomunicacao.accesscontrol.application.service;

import br.com.unimedceara.portalcomunicacao.accesscontrol.domain.model.ColaboradorStatus;
import br.com.unimedceara.portalcomunicacao.accesscontrol.domain.model.JwtAuthenticatedPrincipal;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.ColaboradorEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository.ColaboradorRepository;
import br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest.dto.AuthenticatedUserResponse;
import br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest.dto.CompletePrimeiroAcessoRequest;
import br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest.dto.PrimeiroAcessoAreaResponse;
import br.com.unimedceara.portalcomunicacao.organization.application.service.SingularDomainService;
import br.com.unimedceara.portalcomunicacao.organization.domain.model.AreaStatus;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.SingularEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.AreaRepository;
import br.com.unimedceara.portalcomunicacao.shared.constants.SecurityConstants;
import br.com.unimedceara.portalcomunicacao.shared.exception.BusinessException;
import br.com.unimedceara.portalcomunicacao.shared.exception.ConflictException;
import br.com.unimedceara.portalcomunicacao.shared.exception.ForbiddenException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

/**
 * Onboarding de Primeiro Acesso: listar áreas da Singular resolvida e criar COLABORADOR.
 */
@Service
@ConditionalOnProperty(prefix = "application.persistence", name = "enabled", havingValue = "true", matchIfMissing = true)
public class PrimeiroAcessoApplicationService {

    private static final String DOMAIN_NO_SINGULAR_MESSAGE =
            "Não foi possível determinar a Singular a partir do domínio autenticado";

    private final SingularDomainService singularDomainService;
    private final AreaRepository areaRepository;
    private final ColaboradorRepository colaboradorRepository;
    private final ColaboradorDomainService colaboradorDomainService;
    private final AuthenticationService authenticationService;
    private final AuthAuditService authAuditService;

    public PrimeiroAcessoApplicationService(
            SingularDomainService singularDomainService,
            AreaRepository areaRepository,
            ColaboradorRepository colaboradorRepository,
            ColaboradorDomainService colaboradorDomainService,
            AuthenticationService authenticationService,
            AuthAuditService authAuditService) {
        this.singularDomainService = singularDomainService;
        this.areaRepository = areaRepository;
        this.colaboradorRepository = colaboradorRepository;
        this.colaboradorDomainService = colaboradorDomainService;
        this.authenticationService = authenticationService;
        this.authAuditService = authAuditService;
    }

    @Transactional(readOnly = true)
    public List<PrimeiroAcessoAreaResponse> listAreas(JwtAuthenticatedPrincipal principal) {
        ensurePrimeiroAcesso(principal);
        SingularEntity singular = requireResolvedSingular(principal.email());
        return areaRepository
                .findBySingularIdAndAtivoOrderByNomeAsc(singular.getId(), AreaStatus.ACTIVE.toFlag())
                .stream()
                .map(area -> new PrimeiroAcessoAreaResponse(area.getId(), area.getNome(), area.getSigla()))
                .toList();
    }

    @Transactional
    public AuthenticatedUserResponse complete(
            JwtAuthenticatedPrincipal principal,
            CompletePrimeiroAcessoRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {
        ensurePrimeiroAcesso(principal);
        String email = principal.email().trim().toLowerCase();
        String zimbraId = principal.zimbraId();
        if (zimbraId == null || zimbraId.isBlank()) {
            throw new BusinessException("BUSINESS_RULE_VIOLATION", "Identificador Zimbra é obrigatório");
        }

        if (colaboradorRepository.findByEmailIgnoreCase(email).isPresent()
                || colaboradorRepository.findByZimbraId(zimbraId).isPresent()) {
            throw new ConflictException("Colaborador já cadastrado para esta identidade");
        }

        SingularEntity singular = requireResolvedSingular(email);
        ColaboradorDomainService.OrganizationalContext context = colaboradorDomainService.resolveOrganizationalLinks(
                singular.getId(), request.areaId(), request.teamId());
        if (!singular.getId().equals(context.singularId()) || context.areaId() == null) {
            throw new BusinessException("BUSINESS_RULE_VIOLATION", "Área não pertence à singular informada");
        }

        String displayName = principal.name() == null || principal.name().isBlank()
                ? email
                : principal.name().trim();

        ColaboradorEntity colaborador = new ColaboradorEntity();
        colaborador.setFederacaoId(singular.getFederacaoId());
        colaborador.setSingularId(singular.getId());
        colaborador.setAreaId(context.areaId());
        colaborador.setEquipeId(context.teamId());
        colaborador.setNome(displayName);
        colaborador.setEmail(email);
        colaborador.setZimbraId(zimbraId);
        colaborador.setAtivo(ColaboradorStatus.ACTIVE.toFlag());
        colaborador.setDataCadastro(Instant.now());
        ColaboradorEntity persisted = colaboradorRepository.save(colaborador);

        authAuditService.logPrimeiroAcessoCompleted(persisted.getId(), persisted.getAreaId());
        return authenticationService.promoteToOperationalUser(persisted, false, httpRequest, httpResponse);
    }

    private void ensurePrimeiroAcesso(JwtAuthenticatedPrincipal principal) {
        if (principal == null || !principal.primeiroAcesso() || principal.email() == null || principal.email().isBlank()) {
            throw new ForbiddenException("Colaborador sem autorização para acessar o Portal");
        }
    }

    private SingularEntity requireResolvedSingular(String email) {
        return singularDomainService.findActiveByAuthenticatedEmail(email)
                .orElseThrow(() -> new BusinessException(
                        SecurityConstants.PA_DOMAIN_NO_SINGULAR, DOMAIN_NO_SINGULAR_MESSAGE));
    }
}
