package br.com.unimedceara.portalcomunicacao.accesscontrol.application.service;

import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.ColaboradorEntity;
import br.com.unimedceara.portalcomunicacao.configuration.properties.AuthProperties;
import br.com.unimedceara.portalcomunicacao.shared.exception.ForbiddenException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessionAdministratorAuthorizationServiceTest {

    @Mock
    private ColaboradorService colaboradorService;

    @Mock
    private AuthProperties authProperties;

    @InjectMocks
    private SessionAdministratorAuthorizationService authorizationService;

    @Test
    void shouldAllowConfiguredSessionAdministrator() {
        ColaboradorEntity admin = colaborador(42L, "admin@unimedceara.com.br");
        when(colaboradorService.findById(42L)).thenReturn(admin);
        when(authProperties.sessionAdministratorEmails()).thenReturn(List.of("admin@unimedceara.com.br"));

        authorizationService.ensureSessionAdministrator(42L);

        verify(colaboradorService).findById(42L);
    }

    @Test
    void shouldRejectNonAdministratorWithForbidden() {
        ColaboradorEntity user = colaborador(7L, "user@unimedceara.com.br");
        when(colaboradorService.findById(7L)).thenReturn(user);
        when(authProperties.sessionAdministratorEmails()).thenReturn(List.of("admin@unimedceara.com.br"));

        assertThatThrownBy(() -> authorizationService.ensureSessionAdministrator(7L))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("Colaborador sem autorização para revogar sessões administrativamente");
    }

    private ColaboradorEntity colaborador(long id, String email) {
        ColaboradorEntity colaborador = new ColaboradorEntity();
        colaborador.setId(id);
        colaborador.setEmail(email);
        return colaborador;
    }
}
