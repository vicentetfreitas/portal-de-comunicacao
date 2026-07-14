package br.com.unimedceara.portalcomunicacao.accesscontrol.application.service;

import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.ColaboradorEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository.ColaboradorRepository;
import br.com.unimedceara.portalcomunicacao.shared.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ColaboradorDomainServiceTest {

    @Mock
    private ColaboradorRepository colaboradorRepository;

    @Mock
    private br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.SingularRepository singularRepository;

    @Mock
    private br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.AreaRepository areaRepository;

    @Mock
    private br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.EquipeRepository equipeRepository;

    @InjectMocks
    private ColaboradorDomainService colaboradorDomainService;

    @Test
    void shouldRejectSelfAsManager() {
        assertThatThrownBy(() -> colaboradorDomainService.validateManager(5L, 5L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("gestor de si mesmo");
    }

    @Test
    void shouldRejectDeactivationWithActiveSubordinates() {
        ColaboradorEntity colaborador = new ColaboradorEntity();
        colaborador.setId(10L);
        when(colaboradorRepository.existsByGestorIdAndAtivo(10L, "S")).thenReturn(true);

        assertThatThrownBy(() -> colaboradorDomainService.validateDeactivation(colaborador))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("subordinados ativos");
    }
}
