package br.com.unimedceara.portalcomunicacao.accesscontrol.application.service;

import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.PapelAtribuicaoEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.PapelEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository.PapelAtribuicaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PapelAtribuicaoServiceTest {

    private static final long COLABORADOR_ID = 42L;

    @Mock
    private PapelAtribuicaoRepository papelAtribuicaoRepository;

    @InjectMocks
    private PapelAtribuicaoService papelAtribuicaoService;

    @Test
    void shouldAutoSelectWhenExactlyOneEligibleAssignment() {
        PapelAtribuicaoEntity assignment = eligibleAssignment(1L);
        when(papelAtribuicaoRepository.findByColaborador_Id(COLABORADOR_ID)).thenReturn(List.of(assignment));

        Optional<PapelAtribuicaoEntity> resolved = papelAtribuicaoService.resolveAutomatica(COLABORADOR_ID);

        assertThat(resolved).contains(assignment);
    }

    @Test
    void shouldNotAutoSelectWhenMultipleEligibleAssignments() {
        when(papelAtribuicaoRepository.findByColaborador_Id(COLABORADOR_ID))
                .thenReturn(List.of(eligibleAssignment(1L), eligibleAssignment(2L)));

        assertThat(papelAtribuicaoService.resolveAutomatica(COLABORADOR_ID)).isEmpty();
    }

    @Test
    void shouldNotAutoSelectWhenNoEligibleAssignments() {
        when(papelAtribuicaoRepository.findByColaborador_Id(COLABORADOR_ID)).thenReturn(List.of());

        assertThat(papelAtribuicaoService.resolveAutomatica(COLABORADOR_ID)).isEmpty();
    }

    @Test
    void listElegiveisShouldExcludeInactiveAndOutOfVigenciaAssignments() {
        PapelAtribuicaoEntity active = eligibleAssignment(1L);
        PapelAtribuicaoEntity inactive = eligibleAssignment(2L);
        inactive.setAtivo("N");
        PapelAtribuicaoEntity expired = eligibleAssignment(3L);
        expired.setDataFimVigencia(Instant.now().minusSeconds(60));
        PapelAtribuicaoEntity future = eligibleAssignment(4L);
        future.setDataInicioVigencia(Instant.now().plusSeconds(3600));
        when(papelAtribuicaoRepository.findByColaborador_Id(COLABORADOR_ID))
                .thenReturn(List.of(active, inactive, expired, future));

        List<PapelAtribuicaoEntity> eligiveis = papelAtribuicaoService.listElegiveis(COLABORADOR_ID);

        assertThat(eligiveis).containsExactly(active);
    }

    @Test
    void findElegivelShouldReturnEmptyWhenIdIsNull() {

        assertThat(papelAtribuicaoService.findElegivel(COLABORADOR_ID, null)).isEmpty();
    }

    @Test
    void findElegivelShouldReturnEmptyWhenAssignmentBelongsToAnotherColaborador() {
        when(papelAtribuicaoRepository.findByIdAndColaborador_Id(5L, COLABORADOR_ID)).thenReturn(Optional.empty());

        assertThat(papelAtribuicaoService.findElegivel(COLABORADOR_ID, 5L)).isEmpty();
    }

    @Test
    void findElegivelShouldReturnEmptyWhenAssignmentIsInactive() {
        PapelAtribuicaoEntity inactive = eligibleAssignment(5L);
        inactive.setAtivo("N");
        when(papelAtribuicaoRepository.findByIdAndColaborador_Id(5L, COLABORADOR_ID)).thenReturn(Optional.of(inactive));

        assertThat(papelAtribuicaoService.findElegivel(COLABORADOR_ID, 5L)).isEmpty();
    }

    @Test
    void resolveParaRefreshShouldKeepPreviousAssignmentWhenStillEligible() {
        PapelAtribuicaoEntity previous = eligibleAssignment(9L);
        when(papelAtribuicaoRepository.findByIdAndColaborador_Id(9L, COLABORADOR_ID)).thenReturn(Optional.of(previous));

        Optional<PapelAtribuicaoEntity> resolved = papelAtribuicaoService.resolveParaRefresh(COLABORADOR_ID, 9L);

        assertThat(resolved).contains(previous);
    }

    @Test
    void resolveParaRefreshShouldFallBackToAutomaticSelectionWhenPreviousNoLongerEligible() {
        when(papelAtribuicaoRepository.findByIdAndColaborador_Id(9L, COLABORADOR_ID)).thenReturn(Optional.empty());
        PapelAtribuicaoEntity onlyEligible = eligibleAssignment(1L);
        when(papelAtribuicaoRepository.findByColaborador_Id(COLABORADOR_ID)).thenReturn(List.of(onlyEligible));

        Optional<PapelAtribuicaoEntity> resolved = papelAtribuicaoService.resolveParaRefresh(COLABORADOR_ID, 9L);

        assertThat(resolved).contains(onlyEligible);
    }

    @Test
    void resolveParaRefreshShouldReturnEmptyWhenNoPreviousAndMultipleEligible() {
        when(papelAtribuicaoRepository.findByColaborador_Id(COLABORADOR_ID))
                .thenReturn(List.of(eligibleAssignment(1L), eligibleAssignment(2L)));

        assertThat(papelAtribuicaoService.resolveParaRefresh(COLABORADOR_ID, null)).isEmpty();
    }

    private PapelAtribuicaoEntity eligibleAssignment(long id) {
        PapelAtribuicaoEntity assignment = new PapelAtribuicaoEntity();
        assignment.setId(id);
        PapelEntity papel = new PapelEntity();
        papel.setId(1L);
        papel.setNome("PAPEL_TESTE");
        assignment.setPapel(papel);
        assignment.setAtivo("S");
        assignment.setDataInicioVigencia(Instant.now().minusSeconds(60));
        return assignment;
    }
}
