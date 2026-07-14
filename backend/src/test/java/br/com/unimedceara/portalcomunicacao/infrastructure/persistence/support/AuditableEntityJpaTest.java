package br.com.unimedceara.portalcomunicacao.infrastructure.persistence.support;

import br.com.unimedceara.portalcomunicacao.configuration.properties.PersistencePropertiesConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(PersistencePropertiesConfiguration.class)
@TestPropertySource(locations = "classpath:pf-pers-test.properties")
class AuditableEntityJpaTest {

    @Autowired
    private TestAuditableEntityRepository repository;

    @Test
    void shouldPopulateAuditTimestampsOnPersist() {
        TestAuditableEntity entity = new TestAuditableEntity();
        entity.setName("foundation");

        TestAuditableEntity saved = repository.save(entity);
        repository.flush();

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldSupportCrudThroughBaseRepository() {
        TestAuditableEntity entity = new TestAuditableEntity();
        entity.setName("crud");
        TestAuditableEntity saved = repository.save(entity);

        Optional<TestAuditableEntity> found = repository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("crud");

        repository.delete(saved);
        assertThat(repository.findById(saved.getId())).isEmpty();
    }
}
