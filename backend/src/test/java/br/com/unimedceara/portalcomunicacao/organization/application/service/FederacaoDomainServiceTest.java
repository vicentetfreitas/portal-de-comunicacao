package br.com.unimedceara.portalcomunicacao.organization.application.service;

import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.FederacaoEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.FederacaoRepository;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.SingularRepository;
import br.com.unimedceara.portalcomunicacao.shared.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FederacaoDomainServiceTest {

    @Mock
    private FederacaoRepository federacaoRepository;

    @Mock
    private SingularRepository singularRepository;

    @InjectMocks
    private FederacaoDomainService federacaoDomainService;

    @Test
    void shouldRejectDeactivationWithActiveSingulares() {
        FederacaoEntity federacao = new FederacaoEntity();
        federacao.setId(1L);
        when(singularRepository.existsByFederacaoIdAndAtivo(1L, "S")).thenReturn(true);

        assertThatThrownBy(() -> federacaoDomainService.validateDeactivation(federacao))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("singulares ativas");
    }
}
