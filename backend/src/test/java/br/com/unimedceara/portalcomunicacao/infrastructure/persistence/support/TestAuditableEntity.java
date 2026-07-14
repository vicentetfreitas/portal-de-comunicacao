package br.com.unimedceara.portalcomunicacao.infrastructure.persistence.support;

import br.com.unimedceara.portalcomunicacao.infrastructure.persistence.entity.AuditableEntity;
import br.com.unimedceara.portalcomunicacao.infrastructure.persistence.repository.BaseRepository;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "pf_pers_test_entity")
public class TestAuditableEntity extends AuditableEntity {

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
