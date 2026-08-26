package br.com.unimedceara.portalcomunicacao.documento.acceptance;

import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.ColaboradorEntity;
import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.entity.ArquivoBinarioEntity;
import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.entity.CategoriaDocumentalEntity;
import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.entity.DocumentoEntity;
import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.entity.PastaEntity;
import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.entity.PermissaoPastaEntity;
import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.repository.ArquivoBinarioRepository;
import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.repository.CategoriaDocumentalRepository;
import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.repository.DocumentoRepository;
import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.repository.DocumentoVersaoRepository;
import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.repository.PastaRepository;
import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.repository.PermissaoPastaRepository;
import br.com.unimedceara.portalcomunicacao.support.annotation.IntegrationTest;
import br.com.unimedceara.portalcomunicacao.support.base.AbstractMockMvcIntegrationTest;
import br.com.unimedceara.portalcomunicacao.support.fixture.builder.ArquivoBinarioTestBuilder;
import br.com.unimedceara.portalcomunicacao.support.fixture.builder.CategoriaDocumentalTestBuilder;
import br.com.unimedceara.portalcomunicacao.support.fixture.builder.ColaboradorTestBuilder;
import br.com.unimedceara.portalcomunicacao.support.fixture.builder.DocumentoTestBuilder;
import br.com.unimedceara.portalcomunicacao.support.fixture.builder.DocumentoVersaoTestBuilder;
import br.com.unimedceara.portalcomunicacao.support.fixture.builder.PastaTestBuilder;
import br.com.unimedceara.portalcomunicacao.support.fixture.builder.PermissaoPastaTestBuilder;
import br.com.unimedceara.portalcomunicacao.support.security.TestSecurityContextFactory;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Suíte de testes automatizados dos critérios de aceite FT-DOCUMENTO — listagem de pastas
 * (AT-DOCUMENTO-001, AT-DOCUMENTO-003, AT-DOCUMENTO-004).
 */
@IntegrationTest
@Tag("integration.mutating")
class PastaAcceptanceIntegrationTest extends AbstractMockMvcIntegrationTest {

    private static final long FEDERATION_A = 5001L;
    private static final long SINGULAR_A = 5002L;
    private static final long AREA_A = 5003L;
    private static final long AREA_B = 5004L;

    @Autowired
    private PastaRepository pastaRepository;

    @Autowired
    private PermissaoPastaRepository permissaoPastaRepository;

    @Autowired
    private DocumentoRepository documentoRepository;

    @Autowired
    private DocumentoVersaoRepository documentoVersaoRepository;

    @Autowired
    private ArquivoBinarioRepository arquivoBinarioRepository;

    @Autowired
    private CategoriaDocumentalRepository categoriaDocumentalRepository;

    @Test
    @AcceptanceCriterion(value = "AT-DOCUMENTO-001", type = AcceptanceCriterion.TestType.API)
    void atDocumento001_shouldListPastaWithDocumentWhenAreaHasGrant() throws Exception {
        PastaEntity pasta = PastaTestBuilder.nova().nome("Pasta da Área A").persist(pastaRepository);
        PermissaoPastaTestBuilder.paraPasta(pasta.getId())
                .destinatario(PermissaoPastaEntity.DESTINATARIO_AREA, AREA_A)
                .acesso(PermissaoPastaEntity.ACESSO_LEITURA)
                .persist(permissaoPastaRepository);
        seedDocumentoCompleto(pasta.getId(), "Documento Ativo", DocumentoEntity.STATUS_ATIVO);

        mockMvc.perform(get("/api/v1/pastas").cookie(cookieParaArea(AREA_A)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].nome").value("Pasta da Área A"))
                .andExpect(jsonPath("$.data.content[0].documentos[0].nome").value("Documento Ativo"));
    }

    @Test
    @AcceptanceCriterion(value = "AT-DOCUMENTO-001", type = AcceptanceCriterion.TestType.API)
    void atDocumento001_shouldReturnEmptyWhenNoGrantForContext() throws Exception {
        mockMvc.perform(get("/api/v1/pastas").cookie(cookieParaArea(AREA_A)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isEmpty());
    }

    @Test
    @AcceptanceCriterion(value = "AT-DOCUMENTO-003", type = AcceptanceCriterion.TestType.API)
    void atDocumento003_shouldNotLeakPastaGrantedToOtherArea() throws Exception {
        PastaEntity pastaB = PastaTestBuilder.nova().nome("Pasta da Área B").persist(pastaRepository);
        PermissaoPastaTestBuilder.paraPasta(pastaB.getId())
                .destinatario(PermissaoPastaEntity.DESTINATARIO_AREA, AREA_B)
                .acesso(PermissaoPastaEntity.ACESSO_LEITURA)
                .persist(permissaoPastaRepository);

        mockMvc.perform(get("/api/v1/pastas").cookie(cookieParaArea(AREA_A)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isEmpty());
    }

    @Test
    @AcceptanceCriterion(value = "AT-DOCUMENTO-003", type = AcceptanceCriterion.TestType.API)
    void atDocumento003_shouldIncludePastaGrantedAtFederationLevel() throws Exception {
        PastaEntity pasta = PastaTestBuilder.nova().nome("Pasta institucional").persist(pastaRepository);
        PermissaoPastaTestBuilder.paraPasta(pasta.getId())
                .destinatario(PermissaoPastaEntity.DESTINATARIO_FEDERACAO, FEDERATION_A)
                .acesso(PermissaoPastaEntity.ACESSO_LEITURA)
                .persist(permissaoPastaRepository);

        mockMvc.perform(get("/api/v1/pastas").cookie(cookieParaArea(AREA_A)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].nome").value("Pasta institucional"));
    }

    @Test
    @AcceptanceCriterion(value = "AT-DOCUMENTO-004", type = AcceptanceCriterion.TestType.API)
    void atDocumento004_shouldHideExpiredDocumentFromListing() throws Exception {
        PastaEntity pasta = PastaTestBuilder.nova().persist(pastaRepository);
        PermissaoPastaTestBuilder.paraPasta(pasta.getId())
                .destinatario(PermissaoPastaEntity.DESTINATARIO_AREA, AREA_A)
                .acesso(PermissaoPastaEntity.ACESSO_LEITURA)
                .persist(permissaoPastaRepository);
        seedDocumentoCompleto(pasta.getId(), "Documento Ativo", DocumentoEntity.STATUS_ATIVO);
        seedDocumentoCompleto(pasta.getId(), "Documento Expirado", DocumentoEntity.STATUS_EXPIRADO);

        mockMvc.perform(get("/api/v1/pastas").cookie(cookieParaArea(AREA_A)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].documentos.length()").value(1))
                .andExpect(jsonPath("$.data.content[0].documentos[0].nome").value("Documento Ativo"));
    }

    private Cookie cookieParaArea(long areaId) {
        ColaboradorEntity colaborador = ColaboradorTestBuilder.forFederation(FEDERATION_A).persist(colaboradorRepository);
        return TestSecurityContextFactory.jwtCookieWithContext(
                jwtTokenService, colaborador.getId(), FEDERATION_A, SINGULAR_A, areaId, null);
    }

    private void seedDocumentoCompleto(long pastaId, String titulo, String status) {
        ColaboradorEntity autor = ColaboradorTestBuilder.forFederation(FEDERATION_A).persist(colaboradorRepository);
        CategoriaDocumentalEntity categoria = CategoriaDocumentalTestBuilder.nova().persist(categoriaDocumentalRepository);
        DocumentoEntity documento = DocumentoTestBuilder.paraPasta(pastaId, categoria.getId(), autor.getId())
                .titulo(titulo)
                .status(status)
                .persist(documentoRepository);
        ArquivoBinarioEntity arquivo = ArquivoBinarioTestBuilder.novo().persist(arquivoBinarioRepository);
        DocumentoVersaoTestBuilder.atual(documento.getId(), arquivo.getId(), autor.getId())
                .persist(documentoVersaoRepository);
    }
}
