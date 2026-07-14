package br.com.unimedceara.portalcomunicacao.infrastructure.security.filter;

import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

/**
 * Validação estrutural básica de JWT (esqueleto — sem verificação de assinatura ou emissão).
 */
@Component
class JwtStructureValidator {

    private final JsonMapper jsonMapper;

    JwtStructureValidator(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    /**
     * Verifica se o token possui estrutura JWT válida (header.payload.signature em Base64URL).
     *
     * @param token valor bruto do token
     * @return {@code true} quando a estrutura é válida
     */
    boolean hasValidStructure(String token) {
        if (token == null || token.isBlank()) {
            return false;
        }

        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            return false;
        }

        try {
            decodeBase64Url(parts[0]);
            String payloadJson = decodeBase64Url(parts[1]);
            jsonMapper.readTree(payloadJson);
            decodeBase64Url(parts[2]);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Extrai o subject ({@code sub}) do payload quando presente.
     *
     * @param token valor bruto do token
     * @return subject do JWT, se existir
     */
    Optional<String> extractSubject(String token) {
        if (!hasValidStructure(token)) {
            return Optional.empty();
        }

        try {
            String payloadJson = decodeBase64Url(token.split("\\.", 3)[1]);
            JsonNode payload = jsonMapper.readTree(payloadJson);
            JsonNode subject = payload.get("sub");
            if (subject == null || subject.isNull() || subject.asText().isBlank()) {
                return Optional.empty();
            }
            return Optional.of(subject.asText());
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    private String decodeBase64Url(String value) {
        byte[] decoded = Base64.getUrlDecoder().decode(value);
        return new String(decoded, StandardCharsets.UTF_8);
    }
}
