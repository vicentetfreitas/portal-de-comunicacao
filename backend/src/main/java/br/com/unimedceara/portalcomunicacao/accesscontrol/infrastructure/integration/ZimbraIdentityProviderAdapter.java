package br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.integration;

import br.com.unimedceara.portalcomunicacao.accesscontrol.application.port.IdentityCredentialValidator;
import br.com.unimedceara.portalcomunicacao.configuration.properties.ZimbraProperties;
import br.com.unimedceara.portalcomunicacao.infrastructure.integration.client.IdentityProviderClient;
import br.com.unimedceara.portalcomunicacao.infrastructure.integration.client.IdentityValidationRequest;
import br.com.unimedceara.portalcomunicacao.infrastructure.integration.client.IdentityValidationResult;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * Adaptador do contrato legado {@link IdentityProviderClient} para a porta {@link IdentityCredentialValidator}.
 */
@Component
@Profile("!test")
public class ZimbraIdentityProviderAdapter implements IdentityProviderClient {

    private final IdentityCredentialValidator identityCredentialValidator;
    private final ZimbraProperties zimbraProperties;

    public ZimbraIdentityProviderAdapter(
            IdentityCredentialValidator identityCredentialValidator,
            ZimbraProperties zimbraProperties) {
        this.identityCredentialValidator = identityCredentialValidator;
        this.zimbraProperties = zimbraProperties;
    }

    @Override
    public IdentityValidationResult validateIdentity(IdentityValidationRequest request) {
        return identityCredentialValidator.validateOpaqueToken(request.validationToken());
    }

    @Override
    public URI buildAuthorizationUrl(String state, String callbackUrl) {
        return UriComponentsBuilder.fromUriString(zimbraProperties.loginPageUrl())
                .queryParam("state", state)
                .queryParam("callback", callbackUrl)
                .build(true)
                .toUri();
    }
}
