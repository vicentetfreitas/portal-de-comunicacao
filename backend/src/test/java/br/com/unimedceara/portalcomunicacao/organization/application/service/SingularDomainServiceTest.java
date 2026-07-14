package br.com.unimedceara.portalcomunicacao.organization.application.service;

import br.com.unimedceara.portalcomunicacao.organization.domain.model.SingularStatus;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.SingularEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.AreaRepository;
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
class SingularDomainServiceTest {

    @Mock
    private SingularRepository singularRepository;

    @Mock
    private AreaRepository areaRepository;

    @InjectMocks
    private SingularDomainService singularDomainService;

    @Test
    void shouldRejectDuplicateAcronym() {
        when(singularRepository.existsBySiglaIgnoreCase("UNI-CE")).thenReturn(true);

        assertThatThrownBy(() -> singularDomainService.validateUniqueAcronym("UNI-CE", null))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("sigla");
    }

    @Test
    void shouldRejectDuplicateUnimedCode() {
        when(singularRepository.existsByCodigoUnimedIgnoreCase("UC001")).thenReturn(true);

        assertThatThrownBy(() -> singularDomainService.validateUniqueUnimedCode("UC001", null))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("código Unimed");
    }

    @Test
    void shouldRejectDeactivationWithActiveAreas() {
        SingularEntity singular = new SingularEntity();
        singular.setId(1L);
        when(areaRepository.existsBySingularIdAndAtivo(1L, SingularStatus.ACTIVE.toFlag())).thenReturn(true);

        assertThatThrownBy(() -> singularDomainService.validateDeactivation(singular))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("áreas ativas");
    }
}
