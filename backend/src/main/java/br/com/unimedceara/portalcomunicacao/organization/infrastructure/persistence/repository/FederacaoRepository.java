package br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository;

import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.FederacaoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repositório JPA de federações organizacionais.
 */
public interface FederacaoRepository extends JpaRepository<FederacaoEntity, Long> {

    Optional<FederacaoEntity> findByIdAndAtivo(Long id, String ativo);

    boolean existsBySiglaIgnoreCase(String sigla);

    boolean existsBySiglaIgnoreCaseAndIdNot(String sigla, Long id);

    boolean existsByCodigoUnimed(Integer codigoUnimed);

    boolean existsByCodigoUnimedAndIdNot(Integer codigoUnimed, Long id);

    boolean existsByRegistroAns(String registroAns);

    boolean existsByRegistroAnsAndIdNot(String registroAns, Long id);

    @Query("""
            SELECT f FROM FederacaoEntity f
            WHERE (:ativo IS NULL OR f.ativo = :ativo)
              AND (:name IS NULL OR LOWER(f.nome) LIKE LOWER(CONCAT('%', :name, '%')))
              AND (:acronym IS NULL OR LOWER(f.sigla) LIKE LOWER(CONCAT('%', :acronym, '%')))
              AND (:unimedCode IS NULL OR f.codigoUnimed = :unimedCode)
            """)
    Page<FederacaoEntity> findByFilters(
            @Param("ativo") String ativo,
            @Param("name") String name,
            @Param("acronym") String acronym,
            @Param("unimedCode") Integer unimedCode,
            Pageable pageable);
}
