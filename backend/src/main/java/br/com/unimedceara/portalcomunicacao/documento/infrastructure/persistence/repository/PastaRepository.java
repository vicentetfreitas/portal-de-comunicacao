package br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.repository;

import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.entity.PastaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repositório JPA de pastas documentais (FT-DOCUMENTO).
 */
public interface PastaRepository extends JpaRepository<PastaEntity, Long> {

    /**
     * Lista pastas ativas com {@code PERMISSAO_PASTA} (TIP_ACESSO=LEITURA) compatível com
     * algum nível do Contexto Ativo do colaborador (RF-DOCUMENTO-001, RF-DOCUMENTO-003).
     * {@code teamId} é opcional.
     */
    @Query("""
            SELECT p FROM PastaEntity p
            WHERE p.ativo = 'S'
              AND EXISTS (
                  SELECT 1 FROM PermissaoPastaEntity pp
                  WHERE pp.pastaId = p.id
                    AND pp.tipoAcesso = 'LEITURA'
                    AND (
                         (pp.tipoDestinatario = 'FEDERACAO' AND pp.codigoDestinatario = :federationId)
                      OR (pp.tipoDestinatario = 'SINGULAR' AND pp.codigoDestinatario = :singularId)
                      OR (pp.tipoDestinatario = 'AREA' AND pp.codigoDestinatario = :areaId)
                      OR (:teamId IS NOT NULL AND pp.tipoDestinatario = 'EQUIPE' AND pp.codigoDestinatario = :teamId)
                      )
              )
            """)
    Page<PastaEntity> findAccessible(
            @Param("federationId") Long federationId,
            @Param("singularId") Long singularId,
            @Param("areaId") Long areaId,
            @Param("teamId") Long teamId,
            Pageable pageable);
}
