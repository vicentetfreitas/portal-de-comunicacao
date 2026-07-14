package br.com.unimedceara.portalcomunicacao.shared.dto;

/**
 * Representa um erro de validação associado a um campo específico.
 *
 * @param field   nome do campo com erro de validação
 * @param message mensagem descritiva do erro
 */
public record FieldValidationError(String field, String message) {
}
