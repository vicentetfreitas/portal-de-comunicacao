package br.com.unimedceara.portalcomunicacao.accesscontrol.application.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Auditoria estruturada de eventos de autenticação (sem dados sensíveis).
 */
@Slf4j
@Service
public class AuthAuditService {

    /**
     * Registra login bem-sucedido.
     */
    public void logLoginSuccess(long colaboradorId, String sessionId) {
        log.info("auth_event=LOGIN_SUCCESS colaboradorId={} sessionId={}", colaboradorId, sessionId);
    }

    /**
     * Registra emissão de credencial temporária de Primeiro Acesso.
     */
    public void logPrimeiroAcessoLogin(String email) {
        log.info("auth_event=PRIMEIRO_ACESSO_LOGIN email={}", email);
    }

    /**
     * Registra conclusão do Primeiro Acesso (criação de COLABORADOR + sessão operacional).
     */
    public void logPrimeiroAcessoCompleted(long colaboradorId, long areaId) {
        log.info("auth_event=PRIMEIRO_ACESSO_COMPLETED colaboradorId={} areaId={}", colaboradorId, areaId);
    }

    /**
     * Registra falha de login.
     */
    public void logLoginFailure(String reason) {
        log.warn("auth_event=LOGIN_FAILURE reason={}", reason);
    }

    /**
     * Registra logout.
     */
    public void logLogout(long colaboradorId, String sessionId) {
        log.info("auth_event=LOGOUT colaboradorId={} sessionId={}", colaboradorId, sessionId);
    }

    /**
     * Registra renovação de Access Token.
     */
    public void logRefresh(long colaboradorId, String sessionId) {
        log.info("auth_event=TOKEN_REFRESH colaboradorId={} sessionId={}", colaboradorId, sessionId);
    }

    /**
     * Registra revogação administrativa de sessão.
     */
    public void logAdministrativeRevocation(
            long administratorColaboradorId,
            String sessionId,
            long targetColaboradorId) {
        log.info(
                "auth_event=ADMIN_SESSION_REVOCATION administratorColaboradorId={} sessionId={} targetColaboradorId={}",
                administratorColaboradorId,
                sessionId,
                targetColaboradorId);
    }

    /**
     * Registra indisponibilidade do provedor de identidade.
     */
    public void logIdentityProviderUnavailable() {
        log.warn("auth_event=IDENTITY_PROVIDER_UNAVAILABLE");
    }
}
