package br.com.unimedceara.portalcomunicacao.infrastructure.persistence.repository;

import br.com.unimedceara.portalcomunicacao.infrastructure.persistence.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * Repositório base Spring Data JPA para entidades que estendem {@link BaseEntity}.
 *
 * @param <T>  tipo da entidade
 * @param <ID> tipo do identificador
 */
@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity, ID extends Serializable> extends JpaRepository<T, ID> {
}
