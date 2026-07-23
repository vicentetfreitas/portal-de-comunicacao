package br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository;

import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.ColaboradorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repositório JPA de colaboradores.
 */
public interface ColaboradorRepository extends JpaRepository<ColaboradorEntity, Long> {

    Optional<ColaboradorEntity> findByEmailIgnoreCase(String email);

    Optional<ColaboradorEntity> findByZimbraId(String zimbraId);

    boolean existsByEquipeIdAndAtivo(Long equipeId, String ativo);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    boolean existsByZimbraId(String zimbraId);

    boolean existsByZimbraIdAndIdNot(String zimbraId, Long id);

    boolean existsByGestorIdAndAtivo(Long gestorId, String ativo);

    @Query("""
            SELECT c FROM ColaboradorEntity c
            WHERE (:singularId IS NULL OR c.singularId = :singularId)
              AND (:areaId IS NULL OR c.areaId = :areaId)
              AND (:teamId IS NULL OR c.equipeId = :teamId)
              AND (:ativo IS NULL OR c.ativo = :ativo)
              AND (:name IS NULL OR LOWER(c.nome) LIKE LOWER(CONCAT('%', :name, '%')))
              AND (:email IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%')))
            """)
    Page<ColaboradorEntity> findByFilters(
            @Param("singularId") Long singularId,
            @Param("areaId") Long areaId,
            @Param("teamId") Long teamId,
            @Param("ativo") String ativo,
            @Param("name") String name,
            @Param("email") String email,
            Pageable pageable);
}
