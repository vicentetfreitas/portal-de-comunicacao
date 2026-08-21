package br.com.unimedceara.portalcomunicacao.accesscontrol.application.service;

import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.PapelAtribuicaoEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository.PapelAtribuicaoRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Resolução do contexto operacional do colaborador a partir de suas
 * atribuições de papel (PAPEL_ATRIBUICAO) — FT-SESSION.
 * <p>
 * Regra: 1 atribuição elegível seleciona automaticamente; mais de 1 exige
 * seleção explícita do colaborador; nenhuma elegível resulta em contexto
 * sem atribuição ativa. Toda atribuição, incluindo a informada pelo
 * colaborador, é revalidada contra o banco (pertencimento, status, vigência)
 * antes de ser aceita como ativa.
 */
@Service
@ConditionalOnProperty(prefix = "application.persistence", name = "enabled", havingValue = "true", matchIfMissing = true)
public class PapelAtribuicaoService {

    private final PapelAtribuicaoRepository papelAtribuicaoRepository;

    public PapelAtribuicaoService(PapelAtribuicaoRepository papelAtribuicaoRepository) {
        this.papelAtribuicaoRepository = papelAtribuicaoRepository;
    }

    /**
     * Lista as atribuições elegíveis (ativas e vigentes) do colaborador.
     */
    @Transactional(readOnly = true)
    public List<PapelAtribuicaoEntity> listElegiveis(Long colaboradorId) {
        Instant now = Instant.now();
        return papelAtribuicaoRepository.findByColaborador_Id(colaboradorId).stream()
                .filter(atribuicao -> atribuicao.isElegivel(now))
                .toList();
    }

    /**
     * Busca uma atribuição elegível específica, validando pertencimento ao colaborador.
     * Retorna vazio se a atribuição não existir, não pertencer ao colaborador,
     * ou não estiver ativa/vigente — nunca confia apenas no identificador informado.
     */
    @Transactional(readOnly = true)
    public Optional<PapelAtribuicaoEntity> findElegivel(Long colaboradorId, Long papelAtribuicaoId) {
        if (papelAtribuicaoId == null) {
            return Optional.empty();
        }
        Instant now = Instant.now();
        return papelAtribuicaoRepository.findByIdAndColaborador_Id(papelAtribuicaoId, colaboradorId)
                .filter(atribuicao -> atribuicao.isElegivel(now));
    }

    /**
     * Resolve a atribuição ativa inicial (login ou quando não há atribuição prévia):
     * seleciona automaticamente quando há exatamente 1 elegível; caso contrário
     * (0 ou mais de 1), não seleciona — exige ação explícita do colaborador.
     */
    @Transactional(readOnly = true)
    public Optional<PapelAtribuicaoEntity> resolveAutomatica(Long colaboradorId) {
        List<PapelAtribuicaoEntity> elegiveis = listElegiveis(colaboradorId);
        return elegiveis.size() == 1 ? Optional.of(elegiveis.get(0)) : Optional.empty();
    }

    /**
     * Resolve a atribuição ativa a partir de uma candidata (claim do token atual): mantém a
     * candidata se ainda elegível; caso contrário, aplica a mesma regra de seleção automática
     * (1 elegível) usada no login. Usado tanto por {@code /auth/refresh} (preserva a atribuição
     * ativa) quanto por {@code GET /auth/me} (RN-SESSION-007 é regra de estado, não só de
     * login/refresh: reaplica a seleção automática mesmo fora desses dois fluxos).
     */
    @Transactional(readOnly = true)
    public Optional<PapelAtribuicaoEntity> resolveParaRefresh(Long colaboradorId, Long atribuicaoAnteriorId) {
        Optional<PapelAtribuicaoEntity> anterior = findElegivel(colaboradorId, atribuicaoAnteriorId);
        return anterior.isPresent() ? anterior : resolveAutomatica(colaboradorId);
    }
}
