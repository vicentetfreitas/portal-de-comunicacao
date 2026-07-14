package br.com.unimedceara.portalcomunicacao.infrastructure.persistence.entity;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class BaseEntityTest {

    private static final class TestEntity extends BaseEntity {
    }

    @Test
    void shouldBeEqualWhenSameId() {
        UUID id = UUID.randomUUID();
        TestEntity first = new TestEntity();
        first.setId(id);
        TestEntity second = new TestEntity();
        second.setId(id);

        assertThat(first).isEqualTo(second);
        assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentId() {
        TestEntity first = new TestEntity();
        first.setId(UUID.randomUUID());
        TestEntity second = new TestEntity();
        second.setId(UUID.randomUUID());

        assertThat(first).isNotEqualTo(second);
    }

    @Test
    void shouldNotBeEqualWhenIdIsNull() {
        TestEntity first = new TestEntity();
        TestEntity second = new TestEntity();

        assertThat(first).isNotEqualTo(second);
        assertThat(first).isNotEqualTo(null);
        assertThat(first).isNotEqualTo("other");
    }
}
