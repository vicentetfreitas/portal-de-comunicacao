package br.com.unimedceara.portalcomunicacao.documento.acceptance;

import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.ColaboradorEntity;
import br.com.unimedceara.portalcomunicacao.documento.application.port.ObjectStorageClient;
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
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.SingularEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.AreaRepository;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.SingularRepository;
import br.com.unimedceara.portalcomunicacao.support.annotation.IntegrationTest;
import br.com.unimedceara.portalcomunicacao.support.base.AbstractMockMvcIntegrationTest;
import br.com.unimedceara.portalcomunicacao.support.fixture.builder.AreaTestBuilder;
import br.com.unimedceara.portalcomunicacao.support.fixture.builder.ArquivoBinarioTestBuilder;
import br.com.unimedceara.portalcomunicacao.support.fixture.builder.CategoriaDocumentalTestBuilder;
import br.com.unimedceara.portalcomunicacao.support.fixture.builder.ColaboradorTestBuilder;
import br.com.unimedceara.portalcomunicacao.support.fixture.builder.DocumentoTestBuilder;
import br.com.unimedceara.portalcomunicacao.support.fixture.builder.DocumentoVersaoTestBuilder;
import br.com.unimedceara.portalcomunicacao.support.fixture.builder.PastaTestBuilder;
import br.com.unimedceara.portalcomunicacao.support.fixture.builder.PermissaoPastaTestBuilder;
import br.com.unimedceara.portalcomunicacao.support.fixture.builder.SingularTestBuilder;
import br.com.unimedceara.portalcomunicacao.support.data.IntegrationTestUniqueData;
import br.com.unimedceara.portalcomunicacao.support.security.TestSecurityContextFactory;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.io.ByteArrayInputStream;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Suíte de testes automatizados dos critérios de aceite FT-DOCUMENTO — download de documento
 * (AT-DOCUMENTO-002, AT-DOCUMENTO-003, AT-DOCUMENTO-004). {@link ObjectStorageClient} é mockado —
 * não depende de MinIO real (mesmo racional de excluir dependência externa não provisionada
 * da suíte padrão, ver Opção B em backend.yml).
 */
@IntegrationTest
@Tag("integration.mutating")
class DocumentoAcceptanceIntegrationTest extends AbstractMockMvcIntegrationTest {

    @Autowired
    private SingularRepository singularRepository;

    @Autowired
    private AreaRepository areaRepository;

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

    @MockitoBean
    private ObjectStorageClient objectStorageClient;

    private long federacaoId;
    private long singularId;
    private long areaAId;
    private long areaBId;

    @BeforeEach
    void seedOrganizationHierarchy() {
        federacaoId = authProperties.defaultFederationId();
        SingularEntity singular = SingularTestBuilder.forFederation(federacaoId).persist(singularRepository);
        singularId = singular.getId();
        areaAId = AreaTestBuilder.forSingular(singularId).nome("Área A").persist(areaRepository).getId();
        areaBId = AreaTestBuilder.forSingular(singularId).nome("Área B").persist(areaRepository).getId();
    }

    @Test
    @AcceptanceCriterion(value = "AT-DOCUMENTO-002", type = AcceptanceCriterion.TestType.API)
    void atDocumento002_shouldDownloadDocumentSuccessfully() throws Exception {
        when(objectStorageClient.download(anyString()))
                .thenReturn(new ByteArrayInputStream("conteudo-teste".getBytes()));

        PastaEntity pasta = pastaComPermissao(areaAId, PermissaoPastaEntity.ACESSO_DOWNLOAD);
        DocumentoEntity documento = seedDocumentoCompleto(pasta.getId(), DocumentoEntity.STATUS_ATIVO, "relatorio.pdf");

        mockMvc.perform(get("/api/v1/documentos/" + documento.getId() + "/download")
                        .cookie(cookieParaArea(areaAId)))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"relatorio.pdf\""));
    }

    @Test
    @AcceptanceCriterion(value = "AT-DOCUMENTO-002", type = AcceptanceCriterion.TestType.API)
    void atDocumento002_shouldReturn404WhenDocumentDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/v1/documentos/999999/download").cookie(cookieParaArea(areaAId)))
                .andExpect(status().isNotFound());
    }

    @Test
    @AcceptanceCriterion(value = "AT-DOCUMENTO-003", type = AcceptanceCriterion.TestType.API)
    void atDocumento003_shouldDenyDownloadWithoutMatchingGrant() throws Exception {
        PastaEntity pasta = pastaComPermissao(areaBId, PermissaoPastaEntity.ACESSO_DOWNLOAD);
        DocumentoEntity documento = seedDocumentoCompleto(pasta.getId(), DocumentoEntity.STATUS_ATIVO, "confidencial.pdf");

        mockMvc.perform(get("/api/v1/documentos/" + documento.getId() + "/download")
                        .cookie(cookieParaArea(areaAId)))
                .andExpect(status().isForbidden());
    }

    @Test
    @AcceptanceCriterion(value = "AT-DOCUMENTO-004", type = AcceptanceCriterion.TestType.API)
    void atDocumento004_shouldReturn404WhenDocumentExpired() throws Exception {
        PastaEntity pasta = pastaComPermissao(areaAId, PermissaoPastaEntity.ACESSO_DOWNLOAD);
        DocumentoEntity documento = seedDocumentoCompleto(pasta.getId(), DocumentoEntity.STATUS_EXPIRADO, "vencido.pdf");

        mockMvc.perform(get("/api/v1/documentos/" + documento.getId() + "/download")
                        .cookie(cookieParaArea(areaAId)))
                .andExpect(status().isNotFound());
    }

    @Test
    @AcceptanceCriterion(value = "AT-DOCUMENTO-004", type = AcceptanceCriterion.TestType.API)
    void atDocumento004_shouldAllowDownloadWhenArchived() throws Exception {
        when(objectStorageClient.download(anyString()))
                .thenReturn(new ByteArrayInputStream("conteudo-teste".getBytes()));

        PastaEntity pasta = pastaComPermissao(areaAId, PermissaoPastaEntity.ACESSO_DOWNLOAD);
        DocumentoEntity documento = seedDocumentoCompleto(pasta.getId(), DocumentoEntity.STATUS_ARQUIVADO, "historico.pdf");

        mockMvc.perform(get("/api/v1/documentos/" + documento.getId() + "/download")
                        .cookie(cookieParaArea(areaAId)))
                .andExpect(status().isOk());
    }

    private PastaEntity pastaComPermissao(long areaId, String tipoAcesso) {
        PastaEntity pasta = PastaTestBuilder.nova().persist(pastaRepository);
        PermissaoPastaTestBuilder.paraPasta(pasta.getId())
                .destinatario(PermissaoPastaEntity.DESTINATARIO_AREA, areaId)
                .acesso(tipoAcesso)
                .persist(permissaoPastaRepository);
        return pasta;
    }

    private Cookie cookieParaArea(long areaId) {
        ColaboradorEntity colaborador = ColaboradorTestBuilder.forFederation(federacaoId)
                .zimbraId("zimbra-" + IntegrationTestUniqueData.uniqueId())
                .persist(colaboradorRepository);
        return TestSecurityContextFactory.jwtCookieWithContext(
                jwtTokenService, colaborador.getId(), federacaoId, singularId, areaId, null);
    }

    private DocumentoEntity seedDocumentoCompleto(long pastaId, String status, String nomeArquivo) {
        ColaboradorEntity autor = ColaboradorTestBuilder.forFederation(federacaoId)
                .zimbraId("zimbra-" + IntegrationTestUniqueData.uniqueId())
                .persist(colaboradorRepository);
        CategoriaDocumentalEntity categoria = CategoriaDocumentalTestBuilder.nova().persist(categoriaDocumentalRepository);
        DocumentoEntity documento = DocumentoTestBuilder.paraPasta(pastaId, categoria.getId(), autor.getId())
                .status(status)
                .persist(documentoRepository);
        ArquivoBinarioEntity arquivo = ArquivoBinarioTestBuilder.novo().nomeArquivo(nomeArquivo).persist(arquivoBinarioRepository);
        DocumentoVersaoTestBuilder.atual(documento.getId(), arquivo.getId(), autor.getId())
                .persist(documentoVersaoRepository);
        return documento;
    }
}
