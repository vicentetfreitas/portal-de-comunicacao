package br.com.unimedceara.portalcomunicacao.accesscontrol.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RefreshTokenServiceTest {

    private RefreshTokenService refreshTokenService;

    @BeforeEach
    void setUp() {
        refreshTokenService = new RefreshTokenService();
    }

    @Test
    void shouldGenerateUniqueOpaqueTokens() {
        String first = refreshTokenService.generateToken();
        String second = refreshTokenService.generateToken();

        assertThat(first).isNotBlank();
        assertThat(second).isNotBlank();
        assertThat(first).isNotEqualTo(second);
    }

    @Test
    void shouldProduceDeterministicSha256Hash() {
        String token = "550e8400-e29b-41d4-a716-446655440000";
        String hash = refreshTokenService.hashToken(token);

        assertThat(hash).hasSize(64);
        assertThat(refreshTokenService.hashToken(token)).isEqualTo(hash);
    }

    @Test
    void shouldProduceDifferentHashesForDifferentTokens() {
        String hash1 = refreshTokenService.hashToken(refreshTokenService.generateToken());
        String hash2 = refreshTokenService.hashToken(refreshTokenService.generateToken());

        assertThat(hash1).isNotEqualTo(hash2);
    }
}
