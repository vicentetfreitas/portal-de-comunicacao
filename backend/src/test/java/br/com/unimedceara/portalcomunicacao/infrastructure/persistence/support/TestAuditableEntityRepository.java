package br.com.unimedceara.portalcomunicacao.infrastructure.persistence.support;

import br.com.unimedceara.portalcomunicacao.infrastructure.persistence.repository.BaseRepository;

import java.util.UUID;

public interface TestAuditableEntityRepository extends BaseRepository<TestAuditableEntity, UUID> {
}
