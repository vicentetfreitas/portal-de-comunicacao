package br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository;

import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.PapelAtribuicaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório JPA de atribuições de papel ({@code PAPEL_ATRIBUICAO}).
 */
public interface PapelAtribuicaoRepository extends JpaRepository<PapelAtribuicaoEntity, Long> {

    List<PapelAtribuicaoEntity> findByColaborador_Id(Long colaboradorId);

    Optional<PapelAtribuicaoEntity> findByIdAndColaborador_Id(Long id, Long colaboradorId);
}
