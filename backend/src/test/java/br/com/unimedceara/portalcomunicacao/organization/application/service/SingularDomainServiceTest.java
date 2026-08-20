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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
                .hasMessage("Sigla já cadastrada");
    }

    @Test
    void shouldRejectDuplicateUnimedCode() {
        when(singularRepository.existsByCodigoUnimed(1)).thenReturn(true);

        assertThatThrownBy(() -> singularDomainService.validateUniqueUnimedCode(1, null))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Código Unimed já cadastrado");
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

    @Test
    void shouldResolveActiveSingularByAuthenticatedEmailDomain() {
        SingularEntity singular = new SingularEntity();
        singular.setId(10L);
        singular.setFederacaoId(1L);
        when(singularRepository.findByDominioEmailIgnoreCaseAndAtivo(
                "unimedceara.com.br", SingularStatus.ACTIVE.toFlag()))
                .thenReturn(Optional.of(singular));

        Optional<SingularEntity> found = singularDomainService.findActiveByAuthenticatedEmail(
                "  User@UnimedCeara.COM.BR  ");

        assertThat(found).contains(singular);
    }

    @Test
    void shouldNotResolveUnknownOrInvalidEmailDomain() {
        when(singularRepository.findByDominioEmailIgnoreCaseAndAtivo(
                "desconhecido.test", SingularStatus.ACTIVE.toFlag()))
                .thenReturn(Optional.empty());

        assertThat(singularDomainService.findActiveByAuthenticatedEmail("user@desconhecido.test"))
                .isEmpty();
        assertThat(singularDomainService.findActiveByAuthenticatedEmail("sem-arroba"))
                .isEmpty();
        assertThat(singularDomainService.findActiveByAuthenticatedEmail("a@b@c.test"))
                .isEmpty();
        assertThat(singularDomainService.findActiveByAuthenticatedEmail(null))
                .isEmpty();
    }

    @Test
    void shouldExtractNormalizedEmailDomain() {
        assertThat(SingularDomainService.emailDomain("  Ana@UnimedCariri.COM.BR "))
                .isEqualTo("unimedcariri.com.br");
        assertThat(SingularDomainService.emailDomain("invalido")).isNull();
    }
}
