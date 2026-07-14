package br.com.unimedceara.portalcomunicacao.organization.application.service;

import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository.ColaboradorRepository;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.AreaEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.SingularEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.AreaRepository;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.EquipeRepository;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.SingularRepository;
import br.com.unimedceara.portalcomunicacao.shared.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AreaDomainServiceTest {

    @Mock
    private SingularRepository singularRepository;

    @Mock
    private AreaRepository areaRepository;

    @Mock
    private EquipeRepository equipeRepository;

    @Mock
    private ColaboradorRepository colaboradorRepository;

    @InjectMocks
    private AreaDomainService areaDomainService;

    @Test
    void shouldRejectInactiveSingular() {
        SingularEntity singular = new SingularEntity();
        singular.setId(1L);
        singular.setAtivo("N");
        when(singularRepository.findById(1L)).thenReturn(Optional.of(singular));

        assertThatThrownBy(() -> areaDomainService.validateActiveSingular(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Singular inativa");
    }

    @Test
    void shouldRejectHierarchyCycle() {
        AreaEntity area = new AreaEntity();
        area.setId(1L);

        AreaEntity parent = new AreaEntity();
        parent.setId(2L);
        parent.setParentAreaId(1L);

        when(areaRepository.findById(2L)).thenReturn(Optional.of(parent));

        assertThatThrownBy(() -> areaDomainService.validateHierarchyCycle(1L, 2L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("ciclo");
    }
}
