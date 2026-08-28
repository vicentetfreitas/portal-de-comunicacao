package br.com.unimedceara.portalcomunicacao.infrastructure.integration.client;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IdentityProviderClientTest {

    @Test
    void shouldValidateIdentityWithMockImplementation() {
        IdentityProviderClient client = new IdentityProviderClient() {
            @Override
            public IdentityValidationResult validateIdentity(IdentityValidationRequest request) {
                return new IdentityValidationResult(
                        "vicentefreitas@unimedceara.com.br",
                        "Colaborador Teste",
                        "zimbra-id-12345");
            }

            @Override
            public java.net.URI buildAuthorizationUrl(String state, String callbackUrl) {
                return java.net.URI.create("http://localhost/zimbra/auth");
            }
        };

        IdentityValidationResult result = client.validateIdentity(
                new IdentityValidationRequest("callback-token-abc"));

        assertThat(result.email()).isEqualTo("vicentefreitas@unimedceara.com.br");
        assertThat(result.displayName()).isEqualTo("Colaborador Teste");
        assertThat(result.zimbraId()).isEqualTo("zimbra-id-12345");
    }
}
