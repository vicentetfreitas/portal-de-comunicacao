package br.com.unimedceara.portalcomunicacao.support.fixture.builder;

import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.ColaboradorEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository.ColaboradorRepository;
import br.com.unimedceara.portalcomunicacao.organization.domain.model.EquipeStatus;
import br.com.unimedceara.portalcomunicacao.support.data.IntegrationTestUniqueData;

import java.time.Instant;

public final class ColaboradorTestBuilder {

    /**
     * Identidade do administrador de sessão nos testes. Deve permanecer igual à entrada de
     * {@code application.auth.session-administrator-emails} em {@code application-test.yaml} —
     * é o gate de autorização das operações administrativas e de CRUD organizacional.
     */
    public static final String SESSION_ADMINISTRATOR_EMAIL = "vicentefreitas@unimedceara.com.br";

    private long federacaoId;
    private Long singularId;
    private Long areaId;
    private Long equipeId;
    private String nome = "Colaborador Teste";
    private String email = IntegrationTestUniqueData.colaboradorEmail("colab");
    private String zimbraId = "zimbra-test";
    private String ativo = EquipeStatus.ACTIVE.toFlag();

    private ColaboradorTestBuilder() {}

    public static ColaboradorTestBuilder forFederation(long federacaoId) {
        ColaboradorTestBuilder builder = new ColaboradorTestBuilder();
        builder.federacaoId = federacaoId;
        return builder;
    }

    public static ColaboradorTestBuilder sessionAdministrator(long federacaoId) {
        return forFederation(federacaoId)
                .email(SESSION_ADMINISTRATOR_EMAIL)
                .nome("Admin Teste")
                .zimbraId("zimbra-" + SESSION_ADMINISTRATOR_EMAIL);
    }

    public ColaboradorTestBuilder singularId(Long singularId) {
        this.singularId = singularId;
        return this;
    }

    public ColaboradorTestBuilder areaId(Long areaId) {
        this.areaId = areaId;
        return this;
    }

    public ColaboradorTestBuilder equipeId(Long equipeId) {
        this.equipeId = equipeId;
        return this;
    }

    public ColaboradorTestBuilder nome(String nome) {
        this.nome = nome;
        return this;
    }

    public ColaboradorTestBuilder email(String email) {
        this.email = email;
        return this;
    }

    public ColaboradorTestBuilder zimbraId(String zimbraId) {
        this.zimbraId = zimbraId;
        return this;
    }

    public ColaboradorTestBuilder ativo(String ativo) {
        this.ativo = ativo;
        return this;
    }

    public ColaboradorEntity build() {
        ColaboradorEntity colaborador = new ColaboradorEntity();
        colaborador.setFederacaoId(federacaoId);
        colaborador.setSingularId(singularId);
        colaborador.setAreaId(areaId);
        colaborador.setEquipeId(equipeId);
        colaborador.setNome(nome);
        colaborador.setEmail(email);
        colaborador.setZimbraId(zimbraId);
        colaborador.setAtivo(ativo);
        colaborador.setDataCadastro(Instant.now());
        return colaborador;
    }

    public ColaboradorEntity persist(ColaboradorRepository repository) {
        return repository.save(build());
    }

    /**
     * Retorna o colaborador já existente com este e-mail (schema compartilhado, sem limpeza —
     * DEC-DB-023) ou cria um novo. Evita violação de {@code UK_COLABORADOR_EMAIL} para
     * identidades fixas reutilizadas entre testes/execuções.
     */
    public ColaboradorEntity persistOrGet(ColaboradorRepository repository) {
        return repository.findByEmailIgnoreCase(email).orElseGet(() -> repository.save(build()));
    }
}
