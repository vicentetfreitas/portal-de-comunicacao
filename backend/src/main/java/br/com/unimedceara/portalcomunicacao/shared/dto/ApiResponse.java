package br.com.unimedceara.portalcomunicacao.shared.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;

/**
 * Representa uma resposta de sucesso padronizada da API.
 *
 * @param <T> tipo dos dados transportados na resposta
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        @JsonFormat(shape = JsonFormat.Shape.STRING) Instant timestamp,
        boolean success,
        String message,
        T data) {

    /**
     * Cria uma resposta de sucesso contendo apenas os dados.
     *
     * @param data dados da resposta
     * @param <T>  tipo dos dados
     * @return resposta de sucesso
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(Instant.now(), true, null, data);
    }

    /**
     * Cria uma resposta de sucesso com mensagem e dados.
     *
     * @param message mensagem descritiva da operação
     * @param data    dados da resposta
     * @param <T>     tipo dos dados
     * @return resposta de sucesso
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(Instant.now(), true, message, data);
    }
}
