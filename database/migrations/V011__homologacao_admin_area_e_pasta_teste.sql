--------------------------------------------------------------------------------
-- Projeto : Portal de Comunicação
-- Arquivo : V011__homologacao_admin_area_e_pasta_teste.sql
-- Versão  : 1.1  (literal da pasta sem em-dash — corrompido por cliente SQL ao colar)
--
-- Objetivo
--   Provisionar dados institucionais MÍNIMOS para exercitar o fluxo de Gestão
--   Documental (FT-DOCUMENTO / FT-DOCUMENTO-UPLOAD / FT-DOCUMENTO-GESTAO) no
--   ambiente de homologação:
--     1. Catálogo PAPEL (a tabela está VAZIA no TST — o seed ddl/008 usa
--        SQ_PAPEL_COD_PAPEL, inexistente aqui). IDs explícitos 1–4.
--     2. PAPEL_ATRIBUICAO: o colaborador vicentefreitas@unimedceara.com.br
--        (COD_COLABORADOR = 1335) como ADMINISTRADOR da Área "TECNOLOGIA DA
--        INFORMAÇÃO" (COD_AREA = 1). Vigência aberta, ativa. Como é a ÚNICA
--        atribuição elegível dele, o login a seleciona automaticamente
--        (PapelAtribuicaoService.resolveAutomatica).
--     3. Uma pasta-raiz para a Área 1 + grants PERMISSAO_PASTA (LEITURA,
--        DOWNLOAD, EDICAO) no nível AREA/1 — habilita listar, baixar e a
--        escrita (upload, criar subpasta, renomear, mover, arquivar).
--     4. Um documento de exemplo (1 versão) para exercitar editar metadados /
--        mover / arquivar SEM depender do Object Storage.
--
-- Contexto verificado via JDBC (Oracle TST, 2026-08-27)
--   - COLABORADOR 1335: FLG_ATIVO='S', COD_FEDERACAO=1, COD_SINGULAR=2,
--     COD_AREA=1, COD_EQUIPE=NULL — vínculo cadastral já aponta p/ Área 1
--     (a sessão usa esses campos do COLABORADOR, não da atribuição).
--   - PAPEL, PAPEL_ATRIBUICAO, PASTA, PERMISSAO_PASTA: 0 linhas.
--   - CATEGORIA_DOCUMENTAL: 4 linhas (1 Documentos, 2 Imagens, 3 Vídeos, 4 Outros).
--   - Sequences: SQ_PASTA, SQ_PERMISSAO_PASTA, SQ_DOCUMENTO_COD_DOCUMENTO,
--     SQ_DOCUMENTO_VERSAO, SQ_ARQUIVO_BINARIO existem. NÃO existe SQ_PAPEL*.
--
-- Observações
--   - Não é baseline greenfield: dados de homologação. A reconciliação do
--     catálogo PAPEL no ddl/008 (greenfield) segue pendente à parte.
--   - O download do documento de exemplo (item 4) só funciona com o Object
--     Storage/MinIO provisionado — URL_ARQUIVO abaixo é um placeholder. As
--     demais operações (metadados, mover, arquivar, criar subpasta) não
--     dependem do storage.
--
-- Banco       : Oracle Database 11g+
-- Executar como: UNMPORTCOM  (SQL Developer / DBeaver — comandos simples)
-- Pré-check   : VAL-DB-05-verify-homologacao-admin-prereqs.sql
--
-- Decisões: DEC-DB-019 (sem Flyway), DEC-DB-021 (hierarquia institucional)
--------------------------------------------------------------------------------

SET DEFINE OFF


-- 1. Catálogo PAPEL (mesma taxonomia de ddl/008; IDs explícitos) -------------

INSERT INTO UNMPORTCOM.PAPEL (COD_PAPEL, NOM_PAPEL, DSC_PAPEL, FLG_ATIVO, DAT_CADASTRO)
VALUES (1, 'ADMINISTRADOR', 'Administrador do Portal.', 'S', SYSTIMESTAMP);

INSERT INTO UNMPORTCOM.PAPEL (COD_PAPEL, NOM_PAPEL, DSC_PAPEL, FLG_ATIVO, DAT_CADASTRO)
VALUES (2, 'GESTOR_DOCUMENTAL', 'Responsável pela gestão documental.', 'S', SYSTIMESTAMP);

INSERT INTO UNMPORTCOM.PAPEL (COD_PAPEL, NOM_PAPEL, DSC_PAPEL, FLG_ATIVO, DAT_CADASTRO)
VALUES (3, 'EDITOR', 'Responsável pela publicação de conteúdos.', 'S', SYSTIMESTAMP);

INSERT INTO UNMPORTCOM.PAPEL (COD_PAPEL, NOM_PAPEL, DSC_PAPEL, FLG_ATIVO, DAT_CADASTRO)
VALUES (4, 'COLABORADOR', 'Usuário padrão do Portal.', 'S', SYSTIMESTAMP);


-- 2. Atribuição: colaborador 1335 = ADMINISTRADOR da Área 1 (TI) -------------
--    Só COD_AREA populado — atribuição escopada a Área (não a Singular/Federação).

INSERT INTO UNMPORTCOM.PAPEL_ATRIBUICAO (
    COD_PAPEL_ATRIBUICAO, COD_COLABORADOR, COD_PAPEL,
    COD_FEDERACAO, COD_SINGULAR, COD_AREA, COD_EQUIPE,
    DAT_INICIO_VIGENCIA, DAT_FIM_VIGENCIA, FLG_ATIVO)
VALUES (
    1, 1335, 1,
    NULL, NULL, 1, NULL,
    SYSTIMESTAMP, NULL, 'S');


-- 3. Pasta-raiz da Área 1 + grants (LEITURA / DOWNLOAD / EDICAO) --------------

-- Sem em-dash / travessão no literal: caracteres multibyte de 3 bytes (U+2014) são
-- corrompidos por clientes SQL com NLS/charset mal configurado ao colar o script.
INSERT INTO UNMPORTCOM.PASTA (COD_PASTA, COD_PASTA_PAI, NOM_PASTA, DSC_PASTA, FLG_HERDA_PERMISSAO, FLG_ATIVO, DAT_CADASTRO)
VALUES (SQ_PASTA.NEXTVAL, NULL, 'Documentos - Tecnologia da Informacao',
        'Pasta institucional da Area de TI (homologacao).', 'S', 'S', SYSTIMESTAMP);

INSERT INTO UNMPORTCOM.PERMISSAO_PASTA (COD_PERMISSAO_PASTA, COD_PASTA, TIP_DESTINATARIO, COD_DESTINATARIO, TIP_ACESSO, DAT_CADASTRO)
VALUES (SQ_PERMISSAO_PASTA.NEXTVAL, SQ_PASTA.CURRVAL, 'AREA', 1, 'LEITURA', SYSTIMESTAMP);

INSERT INTO UNMPORTCOM.PERMISSAO_PASTA (COD_PERMISSAO_PASTA, COD_PASTA, TIP_DESTINATARIO, COD_DESTINATARIO, TIP_ACESSO, DAT_CADASTRO)
VALUES (SQ_PERMISSAO_PASTA.NEXTVAL, SQ_PASTA.CURRVAL, 'AREA', 1, 'DOWNLOAD', SYSTIMESTAMP);

INSERT INTO UNMPORTCOM.PERMISSAO_PASTA (COD_PERMISSAO_PASTA, COD_PASTA, TIP_DESTINATARIO, COD_DESTINATARIO, TIP_ACESSO, DAT_CADASTRO)
VALUES (SQ_PERMISSAO_PASTA.NEXTVAL, SQ_PASTA.CURRVAL, 'AREA', 1, 'EDICAO', SYSTIMESTAMP);


-- 4. Documento de exemplo na pasta-raiz (1 versão) --------------------------
--    URL_ARQUIVO é PLACEHOLDER — o download só funciona com o Object Storage
--    provisionado. Serve para exercitar editar metadados / mover / arquivar.

INSERT INTO UNMPORTCOM.ARQUIVO_BINARIO (COD_ARQUIVO_BINARIO, NOM_ARQUIVO, URL_ARQUIVO, TIP_MIME, QTD_TAMANHO_BYTES, HASH_ARQUIVO, DAT_CADASTRO)
VALUES (SQ_ARQUIVO_BINARIO.NEXTVAL, 'exemplo-ti.pdf',
        'documentos/homologacao/exemplo-ti.pdf', 'application/pdf', 1024,
        '0000000000000000000000000000000000000000000000000000000000000000', SYSTIMESTAMP);

INSERT INTO UNMPORTCOM.DOCUMENTO (COD_DOCUMENTO, COD_CATEGORIA_DOCUMENTAL, COD_PASTA, COD_COLABORADOR, TIT_DOCUMENTO, DSC_DOCUMENTO, STA_DOCUMENTO, DAT_CADASTRO)
VALUES (SQ_DOCUMENTO_COD_DOCUMENTO.NEXTVAL, 1, SQ_PASTA.CURRVAL, 1335,
        'Documento de exemplo (TI)', 'Documento semeado para homologação.', 'ATIVO', SYSTIMESTAMP);

INSERT INTO UNMPORTCOM.DOCUMENTO_VERSAO (COD_DOCUMENTO_VERSAO, COD_DOCUMENTO, COD_ARQUIVO_BINARIO, COD_COLABORADOR, NUM_VERSAO, DSC_ALTERACAO, FLG_VERSAO_ATUAL, DAT_VERSAO)
VALUES (SQ_DOCUMENTO_VERSAO.NEXTVAL, SQ_DOCUMENTO_COD_DOCUMENTO.CURRVAL, SQ_ARQUIVO_BINARIO.CURRVAL, 1335,
        1, 'Versão inicial (homologação).', 'S', SYSTIMESTAMP);

COMMIT;


-- Se um run anterior gravou o nome corrompido ("Documentos ¿ ..."), corrigir:
--   UPDATE UNMPORTCOM.PASTA SET NOM_PASTA = 'Documentos - Tecnologia da Informacao'
--    WHERE NOM_PASTA LIKE 'Documentos %Tecnologia%'; COMMIT;


-- 5. Conferência (rodar depois de aplicar) --------------------------------

-- papéis (esperado: 4)
SELECT cod_papel, nom_papel, flg_ativo FROM UNMPORTCOM.PAPEL ORDER BY cod_papel;

-- atribuição do colaborador 1335 (esperado: 1 linha, papel ADMINISTRADOR, area 1, ativa)
SELECT pa.cod_papel_atribuicao, p.nom_papel, pa.cod_area, pa.flg_ativo, pa.dat_fim_vigencia
  FROM UNMPORTCOM.PAPEL_ATRIBUICAO pa
  JOIN UNMPORTCOM.PAPEL p ON p.cod_papel = pa.cod_papel
 WHERE pa.cod_colaborador = 1335;

-- pasta + grants (esperado: 1 pasta, 3 grants AREA/1)
SELECT pp.cod_pasta, ps.nom_pasta, pp.tip_destinatario, pp.cod_destinatario, pp.tip_acesso
  FROM UNMPORTCOM.PERMISSAO_PASTA pp
  JOIN UNMPORTCOM.PASTA ps ON ps.cod_pasta = pp.cod_pasta
 ORDER BY pp.tip_acesso;

-- documento de exemplo (esperado: 1 doc ATIVO, 1 versão atual)
SELECT d.cod_documento, d.tit_documento, d.sta_documento, dv.num_versao, dv.flg_versao_atual
  FROM UNMPORTCOM.DOCUMENTO d
  JOIN UNMPORTCOM.DOCUMENTO_VERSAO dv ON dv.cod_documento = d.cod_documento
 WHERE d.cod_colaborador = 1335;
