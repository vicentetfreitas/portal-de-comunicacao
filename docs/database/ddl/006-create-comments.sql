--------------------------------------------------------------------------------
-- Projeto : Portal de Comunicação
-- Arquivo : 006-create-comments.sql
-- Versão  : 2.0
--
-- Objetivo
--   Documentar todas as tabelas e colunas do Portal de Comunicação
--   utilizando o dicionário de dados Oracle.
--
-- Banco
--   Oracle Database 11g+
--
-- Dependências
--   001-create-users.sql
--   002-create-sequences.sql
--   003-create-tables.sql
--   004-create-constraints.sql
--   005-create-indexes.sql
--------------------------------------------------------------------------------

SET DEFINE OFF

PROMPT ==========================================================
PROMPT Portal de Comunicação
PROMPT Documentação do Banco de Dados
PROMPT ==========================================================

--------------------------------------------------------------------------------
-- FEDERACAO
--------------------------------------------------------------------------------

COMMENT ON TABLE FEDERACAO IS
'Federação Unimed administradora do Portal de Comunicação.';

COMMENT ON COLUMN FEDERACAO.COD_FEDERACAO IS 'Identificador da Federação.';
COMMENT ON COLUMN FEDERACAO.NOM_FEDERACAO IS 'Nome da Federação.';
COMMENT ON COLUMN FEDERACAO.SIG_FEDERACAO IS 'Sigla da Federação.';
COMMENT ON COLUMN FEDERACAO.COD_UNIMED IS 'Código oficial da Unimed.';
COMMENT ON COLUMN FEDERACAO.NUM_REGISTRO_ANS IS 'Número de registro na ANS.';
COMMENT ON COLUMN FEDERACAO.URL_SITE IS 'URL do site institucional.';
COMMENT ON COLUMN FEDERACAO.DSC_FEDERACAO IS 'Descrição da Federação.';
COMMENT ON COLUMN FEDERACAO.FLG_ATIVO IS 'Indica se a Federação está ativa.';
COMMENT ON COLUMN FEDERACAO.DAT_CADASTRO IS 'Data de cadastro.';
COMMENT ON COLUMN FEDERACAO.DAT_ATUALIZACAO IS 'Data da última atualização.';

--------------------------------------------------------------------------------
-- SINGULAR
--------------------------------------------------------------------------------

COMMENT ON TABLE SINGULAR IS
'Singulares vinculadas à Federação.';

COMMENT ON COLUMN SINGULAR.COD_SINGULAR IS 'Identificador da Singular.';
COMMENT ON COLUMN SINGULAR.COD_FEDERACAO IS 'Federação à qual pertence.';
COMMENT ON COLUMN SINGULAR.NOM_SINGULAR IS 'Nome da Singular.';
COMMENT ON COLUMN SINGULAR.SIG_SINGULAR IS 'Sigla da Singular.';
COMMENT ON COLUMN SINGULAR.COD_UNIMED IS 'Código oficial da Singular.';
COMMENT ON COLUMN SINGULAR.FLG_ATIVO IS 'Indica se a Singular está ativa.';
COMMENT ON COLUMN SINGULAR.DAT_CADASTRO IS 'Data de cadastro.';
COMMENT ON COLUMN SINGULAR.DAT_ATUALIZACAO IS 'Data da última atualização.';

--------------------------------------------------------------------------------
-- ENDERECO
--------------------------------------------------------------------------------

COMMENT ON TABLE ENDERECO IS
'Endereços físicos vinculados à Federação ou a uma Singular.';

COMMENT ON COLUMN ENDERECO.COD_ENDERECO IS 'Identificador do endereço.';
COMMENT ON COLUMN ENDERECO.COD_FEDERACAO IS 'Federação proprietária (exclusivo com COD_SINGULAR).';
COMMENT ON COLUMN ENDERECO.COD_SINGULAR IS 'Singular proprietária (exclusivo com COD_FEDERACAO).';
COMMENT ON COLUMN ENDERECO.NOM_LOCAL IS 'Nome amigável do local (ex.: Sede Administrativa, Atendimento Centro).';
COMMENT ON COLUMN ENDERECO.TIP_ENDERECO IS 'Classificação do endereço (validada pela aplicação).';
COMMENT ON COLUMN ENDERECO.DES_LOGRADOURO IS 'Logradouro.';
COMMENT ON COLUMN ENDERECO.NUM_ENDERECO IS 'Número.';
COMMENT ON COLUMN ENDERECO.DES_COMPLEMENTO IS 'Complemento.';
COMMENT ON COLUMN ENDERECO.NOM_BAIRRO IS 'Bairro.';
COMMENT ON COLUMN ENDERECO.NOM_CIDADE IS 'Cidade.';
COMMENT ON COLUMN ENDERECO.SIG_UF IS 'Unidade federativa (UF).';
COMMENT ON COLUMN ENDERECO.NUM_CEP IS 'CEP.';
COMMENT ON COLUMN ENDERECO.FLG_PRINCIPAL IS 'Indica endereço principal (S/N).';
COMMENT ON COLUMN ENDERECO.DAT_CADASTRO IS 'Data de cadastro.';
COMMENT ON COLUMN ENDERECO.DAT_ATUALIZACAO IS 'Data da última atualização.';

--------------------------------------------------------------------------------
-- CONTATO
--------------------------------------------------------------------------------

COMMENT ON TABLE CONTATO IS
'Canais de comunicação (Federação, Singular, Área, Equipe ou Colaborador).';

COMMENT ON COLUMN CONTATO.COD_CONTATO IS 'Identificador do contato.';
COMMENT ON COLUMN CONTATO.COD_FEDERACAO IS 'Federação proprietária (exclusivo com demais proprietários).';
COMMENT ON COLUMN CONTATO.COD_SINGULAR IS 'Singular proprietária (exclusivo com demais proprietários).';
COMMENT ON COLUMN CONTATO.COD_AREA IS 'Área proprietária (exclusivo com demais proprietários).';
COMMENT ON COLUMN CONTATO.COD_EQUIPE IS 'Equipe proprietária (exclusivo com demais proprietários).';
COMMENT ON COLUMN CONTATO.COD_COLABORADOR IS 'Colaborador proprietário (exclusivo com demais proprietários).';
COMMENT ON COLUMN CONTATO.TIP_CONTATO IS 'Tipo de canal (validado pela aplicação).';
COMMENT ON COLUMN CONTATO.DSC_CONTATO IS 'Descrição opcional do contato.';
COMMENT ON COLUMN CONTATO.DES_VALOR IS 'Valor do contato (telefone, e-mail, WhatsApp).';
COMMENT ON COLUMN CONTATO.DES_HORARIO IS 'Horário de atendimento.';
COMMENT ON COLUMN CONTATO.FLG_PRINCIPAL IS 'Indica contato principal do tipo (S/N).';
COMMENT ON COLUMN CONTATO.DAT_CADASTRO IS 'Data de cadastro.';
COMMENT ON COLUMN CONTATO.DAT_ATUALIZACAO IS 'Data da última atualização.';

--------------------------------------------------------------------------------
-- AREA
--------------------------------------------------------------------------------

COMMENT ON TABLE AREA IS
'Áreas organizacionais da Federação e das Singulares.';

COMMENT ON COLUMN AREA.COD_AREA IS 'Identificador da Área.';
COMMENT ON COLUMN AREA.COD_SINGULAR IS 'Singular proprietária da área.';
COMMENT ON COLUMN AREA.COD_AREA_PAI IS 'Área superior na hierarquia.';
COMMENT ON COLUMN AREA.NOM_AREA IS 'Nome da área.';
COMMENT ON COLUMN AREA.SIG_AREA IS 'Sigla da área.';
COMMENT ON COLUMN AREA.DSC_AREA IS 'Descrição da área.';
COMMENT ON COLUMN AREA.COD_GESTOR IS 'Colaborador gestor da área (único; DEC-DB-015).';
COMMENT ON COLUMN AREA.FLG_ATIVO IS 'Indica se a área está ativa.';
COMMENT ON COLUMN AREA.DAT_CADASTRO IS 'Data de cadastro.';
COMMENT ON COLUMN AREA.DAT_ATUALIZACAO IS 'Data da última atualização.';

--------------------------------------------------------------------------------
-- EQUIPE
--------------------------------------------------------------------------------

COMMENT ON TABLE EQUIPE IS
'Equipes organizacionais vinculadas às áreas.';

COMMENT ON COLUMN EQUIPE.COD_EQUIPE IS 'Identificador da equipe.';
COMMENT ON COLUMN EQUIPE.COD_AREA IS 'Área responsável.';
COMMENT ON COLUMN EQUIPE.NOM_EQUIPE IS 'Nome da equipe.';
COMMENT ON COLUMN EQUIPE.DSC_EQUIPE IS 'Descrição da equipe.';
COMMENT ON COLUMN EQUIPE.COD_LIDER IS 'Colaborador líder da equipe (único; DEC-DB-015).';
COMMENT ON COLUMN EQUIPE.FLG_ATIVO IS 'Indica se a equipe está ativa.';
COMMENT ON COLUMN EQUIPE.DAT_CADASTRO IS 'Data de cadastro.';
COMMENT ON COLUMN EQUIPE.DAT_ATUALIZACAO IS 'Data da última atualização.';

--------------------------------------------------------------------------------
-- COLABORADOR
--------------------------------------------------------------------------------

COMMENT ON TABLE COLABORADOR IS
'Perfil do colaborador no Portal — atributos intrínsecos e vínculo organizacional; canais em CONTATO.';

COMMENT ON COLUMN COLABORADOR.COD_COLABORADOR IS 'Identificador do colaborador.';
COMMENT ON COLUMN COLABORADOR.COD_FEDERACAO IS 'Federação do colaborador.';
COMMENT ON COLUMN COLABORADOR.COD_SINGULAR IS 'Singular do colaborador.';
COMMENT ON COLUMN COLABORADOR.COD_AREA IS 'Área do colaborador.';
COMMENT ON COLUMN COLABORADOR.COD_EQUIPE IS 'Equipe do colaborador.';
COMMENT ON COLUMN COLABORADOR.COD_GESTOR IS 'Gestor direto do colaborador (FK auto-referência; DEC-DB-016).';
COMMENT ON COLUMN COLABORADOR.NOM_COLABORADOR IS 'Nome completo.';
COMMENT ON COLUMN COLABORADOR.DES_EMAIL IS 'E-mail de identidade/login (FT-AUTH); canais adicionais em CONTATO.';
COMMENT ON COLUMN COLABORADOR.DES_CARGO IS 'Cargo/função (atributo simples; sem entidade CARGO — DEC-DB-016).';
COMMENT ON COLUMN COLABORADOR.ID_ZIMBRA IS 'Identificador do colaborador no Zimbra (IdP).';
COMMENT ON COLUMN COLABORADOR.NUM_CPF IS 'CPF do colaborador.';
COMMENT ON COLUMN COLABORADOR.DES_BIOGRAFIA IS 'Biografia do colaborador.';
COMMENT ON COLUMN COLABORADOR.FLG_ATIVO IS 'Indica se o colaborador está ativo.';
COMMENT ON COLUMN COLABORADOR.DAT_NASCIMENTO IS 'Data de nascimento.';
COMMENT ON COLUMN COLABORADOR.DAT_CONTRATACAO IS 'Data de contratação (aniversário de empresa derivado).';
COMMENT ON COLUMN COLABORADOR.DAT_ULTIMO_ACESSO IS 'Último acesso ao portal.';
COMMENT ON COLUMN COLABORADOR.DAT_CADASTRO IS 'Data de cadastro.';
COMMENT ON COLUMN COLABORADOR.DAT_ATUALIZACAO IS 'Data da última atualização.';

--------------------------------------------------------------------------------
-- ONBOARDING_SOLICITACAO
--------------------------------------------------------------------------------

COMMENT ON TABLE ONBOARDING_SOLICITACAO IS
'Solicitações de cadastro e ativação de acesso ao portal.';

COMMENT ON COLUMN ONBOARDING_SOLICITACAO.COD_ONBOARDING_SOLICITACAO IS
'Identificador da solicitação.';

COMMENT ON COLUMN ONBOARDING_SOLICITACAO.COD_COLABORADOR IS
'Colaborador relacionado à solicitação.';

COMMENT ON COLUMN ONBOARDING_SOLICITACAO.STA_SOLICITACAO IS
'Status da solicitação.';

COMMENT ON COLUMN ONBOARDING_SOLICITACAO.DSC_OBSERVACAO IS
'Observações da análise.';

COMMENT ON COLUMN ONBOARDING_SOLICITACAO.DAT_SOLICITACAO IS
'Data da solicitação.';

COMMENT ON COLUMN ONBOARDING_SOLICITACAO.DAT_PROCESSAMENTO IS
'Data do processamento da solicitação.';

--------------------------------------------------------------------------------
-- GESTÃO DOCUMENTAL
--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
-- CATEGORIA_DOCUMENTAL
--------------------------------------------------------------------------------

COMMENT ON TABLE CATEGORIA_DOCUMENTAL IS
'Categorias utilizadas na classificação dos documentos corporativos.';

COMMENT ON COLUMN CATEGORIA_DOCUMENTAL.COD_CATEGORIA_DOCUMENTAL IS 'Identificador da categoria.';
COMMENT ON COLUMN CATEGORIA_DOCUMENTAL.NOM_CATEGORIA IS 'Nome da categoria documental.';
COMMENT ON COLUMN CATEGORIA_DOCUMENTAL.DSC_CATEGORIA IS 'Descrição da categoria.';
COMMENT ON COLUMN CATEGORIA_DOCUMENTAL.FLG_ATIVO IS 'Indica se a categoria está ativa.';
COMMENT ON COLUMN CATEGORIA_DOCUMENTAL.DAT_CADASTRO IS 'Data de cadastro.';
COMMENT ON COLUMN CATEGORIA_DOCUMENTAL.DAT_ATUALIZACAO IS 'Data da última atualização.';

--------------------------------------------------------------------------------
-- PASTA
--------------------------------------------------------------------------------

COMMENT ON TABLE PASTA IS
'Estrutura hierárquica de armazenamento documental.';

COMMENT ON COLUMN PASTA.COD_PASTA IS 'Identificador da pasta.';
COMMENT ON COLUMN PASTA.COD_PASTA_PAI IS 'Pasta pai.';
COMMENT ON COLUMN PASTA.NOM_PASTA IS 'Nome da pasta.';
COMMENT ON COLUMN PASTA.DSC_PASTA IS 'Descrição da pasta.';
COMMENT ON COLUMN PASTA.FLG_HERDA_PERMISSAO IS 'Indica herança de permissões.';
COMMENT ON COLUMN PASTA.FLG_ATIVO IS 'Indica se a pasta está ativa.';
COMMENT ON COLUMN PASTA.DAT_CADASTRO IS 'Data de cadastro.';
COMMENT ON COLUMN PASTA.DAT_ATUALIZACAO IS 'Data da última atualização.';

--------------------------------------------------------------------------------
-- DOCUMENTO
--------------------------------------------------------------------------------

COMMENT ON TABLE DOCUMENTO IS
'Metadados dos documentos corporativos.';

COMMENT ON COLUMN DOCUMENTO.COD_DOCUMENTO IS 'Identificador do documento.';
COMMENT ON COLUMN DOCUMENTO.COD_CATEGORIA_DOCUMENTAL IS 'Categoria documental.';
COMMENT ON COLUMN DOCUMENTO.COD_PASTA IS 'Pasta onde o documento está armazenado.';
COMMENT ON COLUMN DOCUMENTO.COD_COLABORADOR IS 'Autor do documento.';
COMMENT ON COLUMN DOCUMENTO.TIT_DOCUMENTO IS 'Título do documento.';
COMMENT ON COLUMN DOCUMENTO.DSC_DOCUMENTO IS 'Descrição do documento.';
COMMENT ON COLUMN DOCUMENTO.STA_DOCUMENTO IS 'Status do documento.';
COMMENT ON COLUMN DOCUMENTO.DAT_PUBLICACAO IS 'Data de publicação.';
COMMENT ON COLUMN DOCUMENTO.DAT_EXPIRACAO IS 'Data de expiração.';
COMMENT ON COLUMN DOCUMENTO.DAT_CADASTRO IS 'Data de cadastro.';
COMMENT ON COLUMN DOCUMENTO.DAT_ATUALIZACAO IS 'Data da última atualização.';

--------------------------------------------------------------------------------
-- ARQUIVO_BINARIO
--------------------------------------------------------------------------------

COMMENT ON TABLE ARQUIVO_BINARIO IS
'Metadados dos arquivos armazenados externamente.';

COMMENT ON COLUMN ARQUIVO_BINARIO.COD_ARQUIVO_BINARIO IS 'Identificador do arquivo.';
COMMENT ON COLUMN ARQUIVO_BINARIO.NOM_ARQUIVO IS 'Nome físico do arquivo.';
COMMENT ON COLUMN ARQUIVO_BINARIO.URL_ARQUIVO IS 'Localização do arquivo.';
COMMENT ON COLUMN ARQUIVO_BINARIO.TIP_MIME IS 'Tipo MIME.';
COMMENT ON COLUMN ARQUIVO_BINARIO.QTD_TAMANHO_BYTES IS 'Tamanho em bytes.';
COMMENT ON COLUMN ARQUIVO_BINARIO.HASH_ARQUIVO IS 'Hash para verificação de integridade.';
COMMENT ON COLUMN ARQUIVO_BINARIO.DAT_CADASTRO IS 'Data de cadastro.';

--------------------------------------------------------------------------------
-- DOCUMENTO_VERSAO
--------------------------------------------------------------------------------

COMMENT ON TABLE DOCUMENTO_VERSAO IS
'Histórico de versões dos documentos.';

COMMENT ON COLUMN DOCUMENTO_VERSAO.COD_DOCUMENTO_VERSAO IS 'Identificador da versão.';
COMMENT ON COLUMN DOCUMENTO_VERSAO.COD_DOCUMENTO IS 'Documento relacionado.';
COMMENT ON COLUMN DOCUMENTO_VERSAO.COD_ARQUIVO_BINARIO IS 'Arquivo associado.';
COMMENT ON COLUMN DOCUMENTO_VERSAO.COD_COLABORADOR IS 'Responsável pela versão.';
COMMENT ON COLUMN DOCUMENTO_VERSAO.NUM_VERSAO IS 'Número sequencial da versão.';
COMMENT ON COLUMN DOCUMENTO_VERSAO.DSC_ALTERACAO IS 'Descrição das alterações.';
COMMENT ON COLUMN DOCUMENTO_VERSAO.FLG_VERSAO_ATUAL IS 'Indica se é a versão atual.';
COMMENT ON COLUMN DOCUMENTO_VERSAO.DAT_VERSAO IS 'Data da versão.';

--------------------------------------------------------------------------------
-- COMPARTILHAMENTO
--------------------------------------------------------------------------------

COMMENT ON TABLE COMPARTILHAMENTO IS
'Compartilhamento de documentos, pastas e comunicados.';

COMMENT ON COLUMN COMPARTILHAMENTO.COD_COMPARTILHAMENTO IS 'Identificador do compartilhamento.';
COMMENT ON COLUMN COMPARTILHAMENTO.TIP_ORIGEM IS 'Tipo do recurso compartilhado.';
COMMENT ON COLUMN COMPARTILHAMENTO.COD_ORIGEM IS 'Identificador do recurso.';
COMMENT ON COLUMN COMPARTILHAMENTO.TIP_DESTINATARIO IS 'Tipo do destinatário.';
COMMENT ON COLUMN COMPARTILHAMENTO.COD_DESTINATARIO IS 'Identificador do destinatário.';
COMMENT ON COLUMN COMPARTILHAMENTO.TIP_ACESSO IS 'Nível de acesso concedido.';
COMMENT ON COLUMN COMPARTILHAMENTO.DAT_CADASTRO IS 'Data do compartilhamento.';

--------------------------------------------------------------------------------
-- CONTROLE DE ACESSO
--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
-- AUTH_SESSAO
--------------------------------------------------------------------------------

COMMENT ON TABLE AUTH_SESSAO IS
'Sessões de autenticação do Portal (Refresh Token, revogação e session_id).';

COMMENT ON COLUMN AUTH_SESSAO.COD_SESSAO IS 'Identificador interno da sessão.';
COMMENT ON COLUMN AUTH_SESSAO.ID_SESSAO IS 'Identificador público da sessão (session_id).';
COMMENT ON COLUMN AUTH_SESSAO.COD_COLABORADOR IS 'Colaborador autenticado.';
COMMENT ON COLUMN AUTH_SESSAO.HASH_REFRESH_TOKEN IS 'Hash SHA-256 do Refresh Token opaco.';
COMMENT ON COLUMN AUTH_SESSAO.DES_DISPOSITIVO IS 'Identificação do dispositivo ou navegador.';
COMMENT ON COLUMN AUTH_SESSAO.FLG_REMEMBER_ME IS 'Indica sessão com opção Lembrar-me (S/N).';
COMMENT ON COLUMN AUTH_SESSAO.DAT_CRIACAO IS 'Data de criação da sessão.';
COMMENT ON COLUMN AUTH_SESSAO.DAT_EXPIRACAO IS 'Data de expiração do Refresh Token.';
COMMENT ON COLUMN AUTH_SESSAO.FLG_REVOGADA IS 'Indica sessão revogada (S/N).';
COMMENT ON COLUMN AUTH_SESSAO.DAT_REVOGACAO IS 'Data da revogação da sessão.';

--------------------------------------------------------------------------------
-- PAPEL
--------------------------------------------------------------------------------

COMMENT ON TABLE PAPEL IS
'Perfis de acesso do Portal de Comunicação.';

COMMENT ON COLUMN PAPEL.COD_PAPEL IS 'Identificador do papel.';
COMMENT ON COLUMN PAPEL.NOM_PAPEL IS 'Nome do papel.';
COMMENT ON COLUMN PAPEL.DSC_PAPEL IS 'Descrição do papel.';
COMMENT ON COLUMN PAPEL.FLG_ATIVO IS 'Indica se o papel está ativo.';
COMMENT ON COLUMN PAPEL.DAT_CADASTRO IS 'Data de cadastro.';
COMMENT ON COLUMN PAPEL.DAT_ATUALIZACAO IS 'Data da última atualização.';

--------------------------------------------------------------------------------
-- PAPEL_ATRIBUICAO
--------------------------------------------------------------------------------

COMMENT ON TABLE PAPEL_ATRIBUICAO IS
'Atribuição de papéis aos colaboradores.';

COMMENT ON COLUMN PAPEL_ATRIBUICAO.COD_PAPEL_ATRIBUICAO IS 'Identificador da atribuição.';
COMMENT ON COLUMN PAPEL_ATRIBUICAO.COD_COLABORADOR IS 'Colaborador.';
COMMENT ON COLUMN PAPEL_ATRIBUICAO.COD_PAPEL IS 'Papel atribuído.';
COMMENT ON COLUMN PAPEL_ATRIBUICAO.COD_FEDERACAO IS 'Escopo Federação.';
COMMENT ON COLUMN PAPEL_ATRIBUICAO.COD_SINGULAR IS 'Escopo Singular.';
COMMENT ON COLUMN PAPEL_ATRIBUICAO.COD_AREA IS 'Escopo Área.';
COMMENT ON COLUMN PAPEL_ATRIBUICAO.COD_EQUIPE IS 'Escopo Equipe.';
COMMENT ON COLUMN PAPEL_ATRIBUICAO.DAT_INICIO_VIGENCIA IS 'Início da vigência.';
COMMENT ON COLUMN PAPEL_ATRIBUICAO.DAT_FIM_VIGENCIA IS 'Fim da vigência.';
COMMENT ON COLUMN PAPEL_ATRIBUICAO.FLG_ATIVO IS 'Indica se a atribuição está ativa.';

--------------------------------------------------------------------------------
-- PERMISSAO_PASTA
--------------------------------------------------------------------------------

COMMENT ON TABLE PERMISSAO_PASTA IS
'Permissões explícitas concedidas às pastas.';

COMMENT ON COLUMN PERMISSAO_PASTA.COD_PERMISSAO_PASTA IS 'Identificador da permissão.';
COMMENT ON COLUMN PERMISSAO_PASTA.COD_PASTA IS 'Pasta.';
COMMENT ON COLUMN PERMISSAO_PASTA.TIP_DESTINATARIO IS 'Tipo do destinatário.';
COMMENT ON COLUMN PERMISSAO_PASTA.COD_DESTINATARIO IS 'Destinatário.';
COMMENT ON COLUMN PERMISSAO_PASTA.TIP_ACESSO IS 'Tipo de acesso.';
COMMENT ON COLUMN PERMISSAO_PASTA.DAT_CADASTRO IS 'Data de cadastro.';

--------------------------------------------------------------------------------
-- SOLICITACAO_PERMISSAO
--------------------------------------------------------------------------------

COMMENT ON TABLE SOLICITACAO_PERMISSAO IS
'Solicitações de acesso a documentos e pastas.';

COMMENT ON COLUMN SOLICITACAO_PERMISSAO.COD_SOLICITACAO_PERMISSAO IS 'Identificador da solicitação.';
COMMENT ON COLUMN SOLICITACAO_PERMISSAO.COD_COLABORADOR IS 'Solicitante.';
COMMENT ON COLUMN SOLICITACAO_PERMISSAO.COD_PASTA IS 'Pasta solicitada.';
COMMENT ON COLUMN SOLICITACAO_PERMISSAO.COD_DOCUMENTO IS 'Documento solicitado.';
COMMENT ON COLUMN SOLICITACAO_PERMISSAO.STA_SOLICITACAO IS 'Status da solicitação.';
COMMENT ON COLUMN SOLICITACAO_PERMISSAO.DSC_JUSTIFICATIVA IS 'Justificativa.';
COMMENT ON COLUMN SOLICITACAO_PERMISSAO.DAT_SOLICITACAO IS 'Data da solicitação.';
COMMENT ON COLUMN SOLICITACAO_PERMISSAO.DAT_ANALISE IS 'Data da análise.';

--------------------------------------------------------------------------------
-- REGISTRO_AUDITORIA
--------------------------------------------------------------------------------

COMMENT ON TABLE REGISTRO_AUDITORIA IS
'Histórico de auditoria das operações do portal.';

COMMENT ON COLUMN REGISTRO_AUDITORIA.COD_REGISTRO_AUDITORIA IS 'Identificador do registro.';
COMMENT ON COLUMN REGISTRO_AUDITORIA.COD_COLABORADOR IS 'Colaborador responsável.';
COMMENT ON COLUMN REGISTRO_AUDITORIA.TIP_EVENTO IS 'Tipo do evento.';
COMMENT ON COLUMN REGISTRO_AUDITORIA.TIP_ENTIDADE IS 'Entidade auditada.';
COMMENT ON COLUMN REGISTRO_AUDITORIA.COD_ENTIDADE IS 'Identificador da entidade.';
COMMENT ON COLUMN REGISTRO_AUDITORIA.DADOS_ANTES IS 'Estado anterior.';
COMMENT ON COLUMN REGISTRO_AUDITORIA.DADOS_DEPOIS IS 'Estado posterior.';
COMMENT ON COLUMN REGISTRO_AUDITORIA.DAT_EVENTO IS 'Data e hora do evento.';

--------------------------------------------------------------------------------
-- COMUNICAÇÃO
--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
-- COMUNICADO
--------------------------------------------------------------------------------

COMMENT ON TABLE COMUNICADO IS
'Comunicados institucionais publicados no Portal de Comunicação.';

COMMENT ON COLUMN COMUNICADO.COD_COMUNICADO IS
'Identificador único do comunicado.';

COMMENT ON COLUMN COMUNICADO.COD_COLABORADOR IS
'Colaborador responsável pela publicação do comunicado.';

COMMENT ON COLUMN COMUNICADO.TIT_COMUNICADO IS
'Título do comunicado.';

COMMENT ON COLUMN COMUNICADO.DSC_COMUNICADO IS
'Conteúdo do comunicado.';

COMMENT ON COLUMN COMUNICADO.DAT_PUBLICACAO IS
'Data e hora da publicação.';

COMMENT ON COLUMN COMUNICADO.DAT_EXPIRACAO IS
'Data e hora de expiração do comunicado.';

COMMENT ON COLUMN COMUNICADO.FLG_PUBLICADO IS
'Indica se o comunicado está publicado (S/N).';

COMMENT ON COLUMN COMUNICADO.FLG_DESTAQUE IS
'Indica se o comunicado está em destaque (S/N).';

COMMENT ON COLUMN COMUNICADO.DAT_CADASTRO IS
'Data de cadastro do comunicado.';

COMMENT ON COLUMN COMUNICADO.DAT_ATUALIZACAO IS
'Data da última atualização do comunicado.';

--------------------------------------------------------------------------------
-- NOTIFICACAO
--------------------------------------------------------------------------------

COMMENT ON TABLE NOTIFICACAO IS
'Notificações enviadas aos colaboradores pelo Portal de Comunicação.';

COMMENT ON COLUMN NOTIFICACAO.COD_NOTIFICACAO IS
'Identificador único da notificação.';

COMMENT ON COLUMN NOTIFICACAO.COD_COLABORADOR IS
'Colaborador destinatário da notificação.';

COMMENT ON COLUMN NOTIFICACAO.TIT_NOTIFICACAO IS
'Título da notificação.';

COMMENT ON COLUMN NOTIFICACAO.DSC_NOTIFICACAO IS
'Conteúdo resumido da notificação.';

COMMENT ON COLUMN NOTIFICACAO.TIP_NOTIFICACAO IS
'Tipo da notificação gerada pelo sistema.';

COMMENT ON COLUMN NOTIFICACAO.FLG_LIDA IS
'Indica se a notificação foi lida (S/N).';

COMMENT ON COLUMN NOTIFICACAO.DAT_LEITURA IS
'Data e hora da leitura da notificação.';

COMMENT ON COLUMN NOTIFICACAO.DAT_ENVIO IS
'Data e hora do envio da notificação.';

--------------------------------------------------------------------------------
-- CONFIGURAÇÃO
--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
-- CONFIGURACAO_PORTAL
--------------------------------------------------------------------------------

COMMENT ON TABLE CONFIGURACAO_PORTAL IS
'Configurações globais do Portal de Comunicação por Federação.';

COMMENT ON COLUMN CONFIGURACAO_PORTAL.COD_CONFIGURACAO_PORTAL IS
'Identificador da configuração.';

COMMENT ON COLUMN CONFIGURACAO_PORTAL.COD_FEDERACAO IS
'Federação proprietária da configuração.';

COMMENT ON COLUMN CONFIGURACAO_PORTAL.NOM_PORTAL IS
'Nome do Portal de Comunicação.';

COMMENT ON COLUMN CONFIGURACAO_PORTAL.URL_PORTAL IS
'Endereço principal do portal.';

COMMENT ON COLUMN CONFIGURACAO_PORTAL.URL_LOGO IS
'URL da logomarca institucional.';

COMMENT ON COLUMN CONFIGURACAO_PORTAL.URL_FAVICON IS
'URL do favicon do portal.';

COMMENT ON COLUMN CONFIGURACAO_PORTAL.DSC_RODAPE IS
'Texto exibido no rodapé do portal.';

COMMENT ON COLUMN CONFIGURACAO_PORTAL.FLG_ONBOARDING_ATIVO IS
'Indica se o onboarding está habilitado (S/N).';

COMMENT ON COLUMN CONFIGURACAO_PORTAL.FLG_NOTIFICACAO_EMAIL IS
'Indica se notificações por e-mail estão habilitadas (S/N).';

COMMENT ON COLUMN CONFIGURACAO_PORTAL.FLG_COMUNICADO_DESTAQUE IS
'Indica se comunicados em destaque estão habilitados (S/N).';

COMMENT ON COLUMN CONFIGURACAO_PORTAL.QTD_DIAS_EXPIRACAO_DOCUMENTO IS
'Quantidade padrão de dias para expiração de documentos.';

COMMENT ON COLUMN CONFIGURACAO_PORTAL.DAT_CADASTRO IS
'Data de cadastro da configuração.';

COMMENT ON COLUMN CONFIGURACAO_PORTAL.DAT_ATUALIZACAO IS
'Data da última atualização da configuração.';

--------------------------------------------------------------------------------
-- FIM DO SCRIPT
--------------------------------------------------------------------------------

PROMPT
PROMPT ==========================================================
PROMPT Todos os comentários Oracle foram criados com sucesso.
PROMPT Fim do script 006-create-comments.sql
PROMPT ==========================================================
PROMPT