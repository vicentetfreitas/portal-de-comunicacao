package br.com.unimedceara.portalcomunicacao.documento.domain.service;

import br.com.unimedceara.portalcomunicacao.accesscontrol.domain.model.JwtAuthenticatedPrincipal;
import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.entity.PermissaoPastaEntity;
import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.repository.PermissaoPastaRepository;
import br.com.unimedceara.portalcomunicacao.shared.exception.ForbiddenException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PermissaoPastaDomainServiceTest {

    @Mock
    private PermissaoPastaRepository permissaoPastaRepository;

    @InjectMocks
    private PermissaoPastaDomainService permissaoPastaDomainService;

    private static final JwtAuthenticatedPrincipal PRINCIPAL = new JwtAuthenticatedPrincipal(
            1L, "session", "user@test.com", "Test User", "zimbra-1", false, 10L, 20L, 30L, 40L, null);

    @Test
    void shouldAllowWhenGrantExists() {
        when(permissaoPastaRepository.existsGrant(99L, "LEITURA", 10L, 20L, 30L, 40L)).thenReturn(true);

        assertThatCode(() -> permissaoPastaDomainService.ensureAccess(99L, "LEITURA", PRINCIPAL))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldThrowForbiddenWhenNoGrantExists() {
        when(permissaoPastaRepository.existsGrant(any(), any(), any(), any(), any(), any())).thenReturn(false);

        assertThatThrownBy(() -> permissaoPastaDomainService.ensureAccess(99L, "DOWNLOAD", PRINCIPAL))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    void shouldQueryWithPrincipalContextLevels() {
        when(permissaoPastaRepository.existsGrant(eq(99L), eq("LEITURA"), eq(10L), eq(20L), eq(30L), eq(40L)))
                .thenReturn(true);

        permissaoPastaDomainService.ensureAccess(99L, PermissaoPastaEntity.ACESSO_LEITURA, PRINCIPAL);
    }
}
