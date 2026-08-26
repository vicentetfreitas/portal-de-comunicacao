package br.com.unimedceara.portalcomunicacao.documento.application.port;

import java.io.InputStream;

/**
 * Porta de acesso ao Object Storage S3-compatible (DEC-013). Backend é o único consumidor
 * (ADR-004) — a referência interna ({@code ARQUIVO_BINARIO.URL_ARQUIVO}) nunca é exposta
 * ao cliente; só o binário resolvido por este port.
 */
public interface ObjectStorageClient {

    /**
     * Recupera o conteúdo binário do objeto identificado pela referência interna.
     *
     * @param referenciaObjeto valor de {@code ARQUIVO_BINARIO.URL_ARQUIVO} (chave do objeto)
     * @return stream do conteúdo — responsabilidade do chamador fechar
     */
    InputStream download(String referenciaObjeto);
}
