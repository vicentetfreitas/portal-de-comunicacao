package br.com.unimedceara.portalcomunicacao.shared.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;

/**
 * Representa uma resposta de erro padronizada da API.
 */
@Getter
@EqualsAndHashCode
public class ErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final Instant timestamp;

    private final int status;

    private final String error;

    private final String message;

    private final String path;

    /**
     * Constrói uma resposta de erro.
     *
     * @param timestamp momento em que o erro ocorreu
     * @param status    código HTTP do erro
     * @param error     código identificador do erro
     * @param message   mensagem descritiva do erro
     * @param path      caminho da requisição que originou o erro
     */
    public ErrorResponse(Instant timestamp, int status, String error, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
}
