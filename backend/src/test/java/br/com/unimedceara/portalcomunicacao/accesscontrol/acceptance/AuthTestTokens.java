package br.com.unimedceara.portalcomunicacao.accesscontrol.acceptance;

import br.com.unimedceara.portalcomunicacao.configuration.properties.SecurityProperties;
import tools.jackson.databind.json.JsonMapper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Utilitário de testes para emissão de JWT com expiração controlada.
 */
public final class AuthTestTokens {

    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final String ALGORITHM = "HS256";

    private AuthTestTokens() {
    }

    /**
     * Emite JWT já expirado para cenários AC-AUTH-005.
     */
    public static String expiredAccessToken(
            SecurityProperties properties,
            JsonMapper jsonMapper,
            long colaboradorId,
            String sessionId,
            String email,
            String name) {
        Instant now = Instant.now();
        Instant issuedAt = now.minusSeconds(3600);
        Instant expiration = now.minusSeconds(60);

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("sub", String.valueOf(colaboradorId));
        payload.put("sid", sessionId);
        payload.put("email", email);
        payload.put("name", name);
        payload.put("iat", issuedAt.getEpochSecond());
        payload.put("exp", expiration.getEpochSecond());
        payload.put("iss", properties.jwtIssuer());

        return encodeToken(payload, properties.jwtSecret(), jsonMapper);
    }

    private static String encodeToken(Map<String, Object> payload, String secret, JsonMapper jsonMapper) {
        String headerJson = "{\"alg\":\"" + ALGORITHM + "\",\"typ\":\"JWT\"}";
        String header = base64UrlEncode(headerJson.getBytes(StandardCharsets.UTF_8));
        String body;
        try {
            body = base64UrlEncode(jsonMapper.writeValueAsBytes(payload));
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to serialize JWT payload", ex);
        }
        String signedContent = header + "." + body;
        String signature = base64UrlEncode(sign(signedContent, secret));
        return signedContent + "." + signature;
    }

    private static byte[] sign(String content, String secret) {
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256);
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_SHA256));
            return mac.doFinal(content.getBytes(StandardCharsets.UTF_8));
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to sign JWT", ex);
        }
    }

    private static String base64UrlEncode(byte[] value) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value);
    }
}
