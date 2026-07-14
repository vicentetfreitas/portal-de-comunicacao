package br.com.unimedceara.portalcomunicacao.accesscontrol.application.service;

import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.AuthSessaoEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.ColaboradorEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository.AuthSessaoRepository;
import br.com.unimedceara.portalcomunicacao.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessionAdministrationServiceTest {

    private static final String SESSION_ID = "session-uuid";
    private static final long ADMIN_COLABORADOR_ID = 99L;
    private static final long TARGET_COLABORADOR_ID = 42L;

    @Mock
    private AuthSessaoRepository authSessaoRepository;

    @Mock
    private SessionService sessionService;

    @Mock
    private AuthAuditService authAuditService;

    @Mock
    private SessionAdministratorAuthorizationService sessionAdministratorAuthorizationService;

    @InjectMocks
    private SessionAdministrationService sessionAdministrationService;

    @Test
    void shouldRevokeActiveSessionAndAudit() {
        AuthSessaoEntity sessao = activeSession();
        when(authSessaoRepository.findBySessionId(SESSION_ID)).thenReturn(Optional.of(sessao));

        sessionAdministrationService.revokeSessionAdministratively(SESSION_ID, ADMIN_COLABORADOR_ID);

        verify(sessionAdministratorAuthorizationService).ensureSessionAdministrator(ADMIN_COLABORADOR_ID);
        verify(sessionService).revokeBySessionId(SESSION_ID);
        verify(authAuditService).logAdministrativeRevocation(
                ADMIN_COLABORADOR_ID,
                SESSION_ID,
                TARGET_COLABORADOR_ID);
    }

    @Test
    void shouldBeIdempotentWhenSessionAlreadyRevoked() {
        AuthSessaoEntity sessao = activeSession();
        sessao.setRevogada("S");
        when(authSessaoRepository.findBySessionId(SESSION_ID)).thenReturn(Optional.of(sessao));

        sessionAdministrationService.revokeSessionAdministratively(SESSION_ID, ADMIN_COLABORADOR_ID);

        verify(sessionService, never()).revokeBySessionId(SESSION_ID);
        verify(authAuditService, never()).logAdministrativeRevocation(
                ADMIN_COLABORADOR_ID,
                SESSION_ID,
                TARGET_COLABORADOR_ID);
    }

    @Test
    void shouldThrowWhenSessionNotFound() {
        when(authSessaoRepository.findBySessionId(SESSION_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sessionAdministrationService.revokeSessionAdministratively(
                        SESSION_ID, ADMIN_COLABORADOR_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Sessão não encontrada");
    }

    private AuthSessaoEntity activeSession() {
        ColaboradorEntity colaborador = new ColaboradorEntity();
        colaborador.setId(TARGET_COLABORADOR_ID);

        AuthSessaoEntity sessao = new AuthSessaoEntity();
        sessao.setSessionId(SESSION_ID);
        sessao.setColaborador(colaborador);
        sessao.setRevogada("N");
        return sessao;
    }
}
