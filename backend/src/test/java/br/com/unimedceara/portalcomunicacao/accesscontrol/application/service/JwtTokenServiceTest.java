package br.com.unimedceara.portalcomunicacao.accesscontrol.application.service;

import br.com.unimedceara.portalcomunicacao.accesscontrol.acceptance.AcceptanceCriterion;
import br.com.unimedceara.portalcomunicacao.accesscontrol.domain.model.JwtClaims;
import br.com.unimedceara.portalcomunicacao.configuration.properties.SecurityProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenServiceTest {

    private JwtTokenService jwtTokenService;

    @BeforeEach
    void setUp() {
        SecurityProperties properties = new SecurityProperties(
                "portal-comunicacao",
                "test-jwt-secret-32-characters-minimum",
                15,
                8,
                30,
                3,
                true,
                List.of("http://localhost:4200"));
        jwtTokenService = new JwtTokenService(properties, JsonMapper.builder().build());
    }

    @Test
    void shouldIssueAndValidateTokenWithRequiredClaims() {
        String token = jwtTokenService.issueToken(42L, "session-uuid", "user@unimedceara.com.br", "João Silva");

        Optional<JwtClaims> claims = jwtTokenService.validateAndParse(token);

        assertThat(claims).isPresent();
        assertThat(claims.get().colaboradorId()).isEqualTo(42L);
        assertThat(claims.get().sessionId()).isEqualTo("session-uuid");
        assertThat(claims.get().email()).isEqualTo("user@unimedceara.com.br");
        assertThat(claims.get().name()).isEqualTo("João Silva");
    }

    @Test
    void shouldIssueAndValidateTokenWithColaboradorOrganizationalClaims() {
        String token = jwtTokenService.issueToken(
                42L, "session-uuid", "user@unimedceara.com.br", "João Silva", 1L, 2L, 3L, 4L);

        Optional<JwtClaims> claims = jwtTokenService.validateAndParse(token);

        assertThat(claims).isPresent();
        assertThat(claims.get().federationId()).isEqualTo(1L);
        assertThat(claims.get().singularId()).isEqualTo(2L);
        assertThat(claims.get().areaId()).isEqualTo(3L);
        assertThat(claims.get().teamId()).isEqualTo(4L);
        assertThat(claims.get().primeiroAcesso()).isFalse();
    }

    @Test
    void shouldIssueAndValidatePrimeiroAcessoTokenWithoutColaborador() {
        String token = jwtTokenService.issuePrimeiroAcessoToken(
                "novo@unimedceara.com.br", "Novo Usuario", "zimbra-new");

        Optional<JwtClaims> claims = jwtTokenService.validateAndParse(token);

        assertThat(claims).isPresent();
        assertThat(claims.get().primeiroAcesso()).isTrue();
        assertThat(claims.get().colaboradorId()).isNull();
        assertThat(claims.get().sessionId()).isNull();
        assertThat(claims.get().email()).isEqualTo("novo@unimedceara.com.br");
        assertThat(claims.get().name()).isEqualTo("Novo Usuario");
        assertThat(claims.get().zimbraId()).isEqualTo("zimbra-new");
    }

    @Test
    void shouldRejectTamperedToken() {
        String token = jwtTokenService.issueToken(1L, "sid", "a@b.com", "A");
        String tampered = token.substring(0, token.length() - 2) + "xx";

        assertThat(jwtTokenService.validateAndParse(tampered)).isEmpty();
    }

    @Test
    void shouldRejectInvalidStructure() {
        assertThat(jwtTokenService.validateAndParse("invalid.token")).isEmpty();
    }

    @Test
    @AcceptanceCriterion(value = "AC-AUTH-005", type = AcceptanceCriterion.TestType.UNIT)
    void acAuth005_shouldRejectExpiredAccessToken() {
        String expiredToken = br.com.unimedceara.portalcomunicacao.accesscontrol.acceptance.AuthTestTokens
                .expiredAccessToken(
                        new SecurityProperties(
                                "portal-comunicacao",
                                "test-jwt-secret-32-characters-minimum",
                                15,
                                8,
                                30,
                                3,
                                true,
                                List.of("http://localhost:4200")),
                        tools.jackson.databind.json.JsonMapper.builder().build(),
                        42L,
                        "session-uuid",
                        "user@unimedceara.com.br",
                        "João Silva");

        assertThat(jwtTokenService.validateAndParse(expiredToken)).isEmpty();
    }
}
