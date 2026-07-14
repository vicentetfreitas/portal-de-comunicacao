package br.com.unimedceara.portalcomunicacao.accesscontrol.application.service;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.UUID;

/**
 * Geração de Refresh Tokens opacos (UUID) e hashing SHA-256 para persistência segura.
 */
@Service
public class RefreshTokenService {

    /**
     * Gera um Refresh Token opaco (UUID v4).
     */
    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    /**
     * Calcula o hash SHA-256 hexadecimal do Refresh Token para armazenamento no banco.
     */
    public String hashToken(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 algorithm not available", ex);
        }
    }
}
