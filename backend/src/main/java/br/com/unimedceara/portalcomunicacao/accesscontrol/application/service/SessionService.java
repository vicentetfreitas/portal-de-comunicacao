package br.com.unimedceara.portalcomunicacao.accesscontrol.application.service;

import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.AuthSessaoEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.ColaboradorEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository.AuthSessaoRepository;
import br.com.unimedceara.portalcomunicacao.configuration.properties.SecurityProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Gerenciamento de sessões de autenticação (criação, limite e revogação).
 */
@Service
@ConditionalOnProperty(prefix = "application.persistence", name = "enabled", havingValue = "true", matchIfMissing = true)
public class SessionService {

    private final AuthSessaoRepository authSessaoRepository;
    private final RefreshTokenService refreshTokenService;
    private final SecurityProperties securityProperties;

    public SessionService(
            AuthSessaoRepository authSessaoRepository,
            RefreshTokenService refreshTokenService,
            SecurityProperties securityProperties) {
        this.authSessaoRepository = authSessaoRepository;
        this.refreshTokenService = refreshTokenService;
        this.securityProperties = securityProperties;
    }

    /**
     * Cria sessão com Refresh Token opaco, aplicando limite de sessões simultâneas.
     *
     * @return par (sessionId, rawRefreshToken)
     */
    @Transactional
    public SessionCreationResult createSession(ColaboradorEntity colaborador, boolean rememberMe, String dispositivo) {
        enforceSessionLimit(colaborador.getId());

        String sessionId = UUID.randomUUID().toString();
        String rawRefreshToken = refreshTokenService.generateToken();
        Instant now = Instant.now();
        Instant expiration = rememberMe
                ? now.plusSeconds(securityProperties.refreshTokenRememberMeDays() * 24L * 60L * 60L)
                : now.plusSeconds(securityProperties.refreshTokenTtlHours() * 60L * 60L);

        AuthSessaoEntity sessao = new AuthSessaoEntity();
        sessao.setSessionId(sessionId);
        sessao.setColaborador(colaborador);
        sessao.setRefreshTokenHash(refreshTokenService.hashToken(rawRefreshToken));
        sessao.setDispositivo(dispositivo);
        sessao.setRememberMe(rememberMe ? "S" : "N");
        sessao.setDataCriacao(now);
        sessao.setDataExpiracao(expiration);
        sessao.setRevogada("N");

        authSessaoRepository.save(sessao);
        return new SessionCreationResult(sessionId, rawRefreshToken, rememberMe);
    }

    /**
     * Busca sessão ativa pelo hash do Refresh Token.
     */
    @Transactional(readOnly = true)
    public Optional<AuthSessaoEntity> findActiveByRefreshToken(String rawRefreshToken) {
        return authSessaoRepository.findByRefreshTokenHash(refreshTokenService.hashToken(rawRefreshToken))
                .filter(AuthSessaoEntity::isActive);
    }

    /**
     * Revoga sessão pelo Refresh Token bruto.
     */
    @Transactional
    public void revokeByRefreshToken(String rawRefreshToken) {
        authSessaoRepository.findByRefreshTokenHash(refreshTokenService.hashToken(rawRefreshToken))
                .ifPresent(this::revokeSession);
    }

    /**
     * Revoga sessão pelo identificador público da sessão.
     */
    @Transactional
    public void revokeBySessionId(String sessionId) {
        authSessaoRepository.findBySessionId(sessionId).ifPresent(this::revokeSession);
    }

    private void enforceSessionLimit(long colaboradorId) {
        List<AuthSessaoEntity> activeSessions = authSessaoRepository
                .findByColaborador_IdAndRevogadaAndDataExpiracaoAfterOrderByDataCriacaoAsc(
                        colaboradorId, "N", Instant.now());

        int excess = activeSessions.size() - securityProperties.maxConcurrentSessions() + 1;
        if (excess <= 0) {
            return;
        }

        for (int index = 0; index < excess; index++) {
            revokeSession(activeSessions.get(index));
        }
    }

    private void revokeSession(AuthSessaoEntity sessao) {
        sessao.setRevogada("S");
        sessao.setDataRevogacao(Instant.now());
        authSessaoRepository.save(sessao);
    }

    /**
     * Resultado da criação de sessão com tokens gerados.
     */
    public record SessionCreationResult(String sessionId, String rawRefreshToken, boolean rememberMe) {
    }
}
