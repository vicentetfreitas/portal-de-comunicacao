package br.com.unimedceara.portalcomunicacao.organization.application.service;

import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository.ColaboradorRepository;
import br.com.unimedceara.portalcomunicacao.organization.domain.model.AreaStatus;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.AreaEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.EquipeEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.EquipeRepository;
import br.com.unimedceara.portalcomunicacao.shared.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EquipeDomainServiceTest {

    @Mock
    private AreaDomainService areaDomainService;

    @Mock
    private EquipeRepository equipeRepository;

    @Mock
    private ColaboradorRepository colaboradorRepository;

    @InjectMocks
    private EquipeDomainService equipeDomainService;

    @Test
    void shouldRejectInactiveArea() {
        AreaEntity area = new AreaEntity();
        area.setId(1L);
        area.setAtivo(AreaStatus.INACTIVE.toFlag());
        when(areaDomainService.loadAreaOrThrow(1L)).thenReturn(area);

        assertThatThrownBy(() -> equipeDomainService.validateActiveArea(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Área inativa");
    }

    @Test
    void shouldRejectDeactivationWithActiveColaboradores() {
        EquipeEntity equipe = new EquipeEntity();
        equipe.setId(10L);
        when(colaboradorRepository.existsByEquipeIdAndAtivo(10L, AreaStatus.ACTIVE.toFlag())).thenReturn(true);

        assertThatThrownBy(() -> equipeDomainService.validateDeactivation(equipe))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("colaboradores ativos");
    }
}
