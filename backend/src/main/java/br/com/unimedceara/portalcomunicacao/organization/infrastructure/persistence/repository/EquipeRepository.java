package br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository;

import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.EquipeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repositório JPA de equipes organizacionais.
 */
public interface EquipeRepository extends JpaRepository<EquipeEntity, Long> {

    boolean existsByAreaIdAndAtivo(Long areaId, String ativo);

    boolean existsByAreaIdAndNomeIgnoreCaseAndAtivo(Long areaId, String nome, String ativo);

    boolean existsByAreaIdAndNomeIgnoreCaseAndAtivoAndIdNot(
            Long areaId, String nome, String ativo, Long id);

    @Query("""
            SELECT e FROM EquipeEntity e
            WHERE (:areaId IS NULL OR e.areaId = :areaId)
              AND (:ativo IS NULL OR e.ativo = :ativo)
              AND (:name IS NULL OR LOWER(e.nome) LIKE LOWER(CONCAT('%', :name, '%')))
            """)
    Page<EquipeEntity> findByFilters(
            @Param("areaId") Long areaId,
            @Param("ativo") String ativo,
            @Param("name") String name,
            Pageable pageable);
}
