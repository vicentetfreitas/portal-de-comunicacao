package br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.repository;

import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.entity.PermissaoPastaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repositório JPA de permissões de pasta (FT-DOCUMENTO).
 */
public interface PermissaoPastaRepository extends JpaRepository<PermissaoPastaEntity, Long> {

    /**
     * Verifica se existe grant compatível com algum nível do Contexto Ativo do colaborador
     * (RF-DOCUMENTO-003). {@code teamId} é opcional (Equipe não é obrigatória no vínculo).
     */
    @Query("""
            SELECT COUNT(pp) > 0 FROM PermissaoPastaEntity pp
            WHERE pp.pastaId = :pastaId
              AND pp.tipoAcesso = :tipoAcesso
              AND (
                   (pp.tipoDestinatario = 'FEDERACAO' AND pp.codigoDestinatario = :federationId)
                OR (pp.tipoDestinatario = 'SINGULAR' AND pp.codigoDestinatario = :singularId)
                OR (pp.tipoDestinatario = 'AREA' AND pp.codigoDestinatario = :areaId)
                OR (:teamId IS NOT NULL AND pp.tipoDestinatario = 'EQUIPE' AND pp.codigoDestinatario = :teamId)
              )
            """)
    boolean existsGrant(
            @Param("pastaId") Long pastaId,
            @Param("tipoAcesso") String tipoAcesso,
            @Param("federationId") Long federationId,
            @Param("singularId") Long singularId,
            @Param("areaId") Long areaId,
            @Param("teamId") Long teamId);
}
