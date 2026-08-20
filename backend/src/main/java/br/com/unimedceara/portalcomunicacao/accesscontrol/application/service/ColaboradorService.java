package br.com.unimedceara.portalcomunicacao.accesscontrol.application.service;

import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.ColaboradorEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository.ColaboradorRepository;
import br.com.unimedceara.portalcomunicacao.configuration.properties.AuthProperties;
import br.com.unimedceara.portalcomunicacao.infrastructure.integration.client.IdentityValidationResult;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

/**
 * Localização e criação automática de colaboradores após autenticação Zimbra.
 */
@Service
@ConditionalOnProperty(prefix = "application.persistence", name = "enabled", havingValue = "true", matchIfMissing = true)
public class ColaboradorService {

    private final ColaboradorRepository colaboradorRepository;
    private final AuthProperties authProperties;

    public ColaboradorService(ColaboradorRepository colaboradorRepository, AuthProperties authProperties) {
        this.colaboradorRepository = colaboradorRepository;
        this.authProperties = authProperties;
    }

    /**
     * Localiza colaborador por e-mail ou Zimbra ID, sincronizando identidade quando encontrado.
     */
    @Transactional
    public Optional<ColaboradorEntity> findByIdentity(IdentityValidationResult identity) {
        return colaboradorRepository.findByEmailIgnoreCase(identity.email())
                .or(() -> colaboradorRepository.findByZimbraId(identity.zimbraId()))
                .map(existing -> syncIdentity(existing, identity));
    }

    /**
     * Localiza colaborador por e-mail ou Zimbra ID; cria automaticamente se inexistente.
     */
    @Transactional
    public ColaboradorEntity locateOrCreate(IdentityValidationResult identity) {
        return findByIdentity(identity).orElseGet(() -> createColaborador(identity));
    }

    /**
     * Carrega colaborador por identificador.
     */
    @Transactional(readOnly = true)
    public ColaboradorEntity findById(long colaboradorId) {
        return colaboradorRepository.findById(colaboradorId)
                .orElseThrow(() -> new IllegalStateException("Colaborador não encontrado"));
    }

    private ColaboradorEntity createColaborador(IdentityValidationResult identity) {
        ColaboradorEntity colaborador = new ColaboradorEntity();
        colaborador.setEmail(identity.email().toLowerCase());
        colaborador.setNome(identity.displayName());
        colaborador.setZimbraId(identity.zimbraId());
        colaborador.setAtivo("S");
        colaborador.setFederacaoId(authProperties.defaultFederationId());
        colaborador.setDataCadastro(Instant.now());
        return colaboradorRepository.save(colaborador);
    }

    private ColaboradorEntity syncIdentity(
            ColaboradorEntity colaborador,
            IdentityValidationResult identity) {
        boolean updated = false;

        if (identity.zimbraId() != null
                && !identity.zimbraId().equals(colaborador.getZimbraId())) {
            colaborador.setZimbraId(identity.zimbraId());
            updated = true;
        }

        if (identity.displayName() != null
                && !identity.displayName().isBlank()
                && !identity.displayName().equals(colaborador.getNome())) {
            colaborador.setNome(identity.displayName());
            updated = true;
        }

        if (!updated) {
            return colaborador;
        }

        colaborador.setDataAtualizacao(Instant.now());
        return colaboradorRepository.save(colaborador);
    }
}
