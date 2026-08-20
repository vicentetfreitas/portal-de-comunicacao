package br.com.unimedceara.portalcomunicacao.accesscontrol.application.service;

import br.com.unimedceara.portalcomunicacao.accesscontrol.domain.model.JwtClaims;
import br.com.unimedceara.portalcomunicacao.configuration.properties.SecurityProperties;
import br.com.unimedceara.portalcomunicacao.shared.constants.SecurityConstants;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Emissão e validação criptográfica de Access Tokens JWT (HMAC-SHA256).
 */
@Service
public class JwtTokenService {

    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final String ALGORITHM = "HS256";

    private final SecurityProperties securityProperties;
    private final JsonMapper jsonMapper;
    private final byte[] signingKey;

    public JwtTokenService(SecurityProperties securityProperties, JsonMapper jsonMapper) {
        this.securityProperties = securityProperties;
        this.jsonMapper = jsonMapper;
        this.signingKey = securityProperties.jwtSecret().getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Emite um Access Token JWT com os claims obrigatórios da Feature FT-AUTH.
     */
    public String issueToken(long colaboradorId, String sessionId, String email, String name) {
        return issueToken(colaboradorId, sessionId, email, name, null, null, null, null);
    }

    /**
     * Emite Access Token com vínculos organizacionais do colaborador (claims opcionais).
     */
    public String issueToken(
            long colaboradorId,
            String sessionId,
            String email,
            String name,
            Long federationId,
            Long singularId,
            Long areaId,
            Long teamId) {
        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(securityProperties.jwtAccessTtlMinutes() * 60L);

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("sub", String.valueOf(colaboradorId));
        payload.put("sid", sessionId);
        payload.put("email", email);
        payload.put("name", name);
        if (federationId != null) {
            payload.put("fid", federationId);
        }
        if (singularId != null) {
            payload.put("singularId", singularId);
        }
        if (areaId != null) {
            payload.put("areaId", areaId);
        }
        if (teamId != null) {
            payload.put("teamId", teamId);
        }
        payload.put("iat", now.getEpochSecond());
        payload.put("exp", expiration.getEpochSecond());
        payload.put("iss", securityProperties.jwtIssuer());

        return encodeToken(payload);
    }

    /**
     * Emite Access Token de Primeiro Acesso (identidade Zimbra, sem COLABORADOR / AUTH_SESSAO).
     */
    public String issuePrimeiroAcessoToken(String email, String name, String zimbraId) {
        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(securityProperties.jwtAccessTtlMinutes() * 60L);

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("sub", email);
        payload.put("email", email);
        payload.put("name", name);
        payload.put("zid", zimbraId);
        payload.put("typ", SecurityConstants.JWT_TYP_PRIMEIRO_ACESSO);
        payload.put("iat", now.getEpochSecond());
        payload.put("exp", expiration.getEpochSecond());
        payload.put("iss", securityProperties.jwtIssuer());

        return encodeToken(payload);
    }

    /**
     * Valida assinatura, expiração e estrutura do JWT, retornando os claims quando válido.
     */
    public Optional<JwtClaims> validateAndParse(String token) {
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }

        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            return Optional.empty();
        }

        try {
            String signedContent = parts[0] + "." + parts[1];
            byte[] expectedSignature = sign(signedContent);
            byte[] actualSignature = Base64.getUrlDecoder().decode(parts[2]);
            if (!MessageDigest.isEqual(expectedSignature, actualSignature)) {
                return Optional.empty();
            }

            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            JsonNode payload = jsonMapper.readTree(payloadJson);

            String issuer = readText(payload, "iss");
            if (!securityProperties.jwtIssuer().equals(issuer)) {
                return Optional.empty();
            }

            long expiration = payload.get("exp").asLong();
            if (Instant.now().getEpochSecond() >= expiration) {
                return Optional.empty();
            }

            String subject = readText(payload, "sub");
            String email = readText(payload, "email");
            String name = readText(payload, "name");
            if (subject == null || email == null || name == null) {
                return Optional.empty();
            }

            boolean primeiroAcesso = SecurityConstants.JWT_TYP_PRIMEIRO_ACESSO
                    .equals(readText(payload, "typ"));
            if (primeiroAcesso) {
                String zimbraId = readText(payload, "zid");
                if (zimbraId == null) {
                    return Optional.empty();
                }
                return Optional.of(new JwtClaims(
                        null,
                        null,
                        email,
                        name,
                        zimbraId,
                        true,
                        null,
                        null,
                        null,
                        null));
            }

            String sessionId = readText(payload, "sid");
            if (sessionId == null) {
                return Optional.empty();
            }

            Long federationId = readLong(payload, "fid");
            Long singularId = readLong(payload, "singularId");
            Long areaId = readLong(payload, "areaId");
            Long teamId = readLong(payload, "teamId");

            return Optional.of(new JwtClaims(
                    Long.parseLong(subject),
                    sessionId,
                    email,
                    name,
                    null,
                    false,
                    federationId,
                    singularId,
                    areaId,
                    teamId));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    private String encodeToken(Map<String, Object> payload) {
        String headerJson = "{\"alg\":\"" + ALGORITHM + "\",\"typ\":\"JWT\"}";
        String header = base64UrlEncode(headerJson.getBytes(StandardCharsets.UTF_8));
        String body;
        try {
            body = base64UrlEncode(jsonMapper.writeValueAsBytes(payload));
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to serialize JWT payload", ex);
        }
        String signedContent = header + "." + body;
        String signature = base64UrlEncode(sign(signedContent));
        return signedContent + "." + signature;
    }

    private byte[] sign(String content) {
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256);
            mac.init(new SecretKeySpec(signingKey, HMAC_SHA256));
            return mac.doFinal(content.getBytes(StandardCharsets.UTF_8));
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to sign JWT", ex);
        }
    }

    private String base64UrlEncode(byte[] value) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value);
    }

    private String readText(JsonNode payload, String field) {
        JsonNode node = payload.get(field);
        if (node == null || node.isNull()) {
            return null;
        }
        String text = node.asText();
        return text.isBlank() ? null : text;
    }

    private Long readLong(JsonNode payload, String field) {
        JsonNode node = payload.get(field);
        if (node == null || node.isNull()) {
            return null;
        }
        return node.asLong();
    }
}
