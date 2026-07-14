package br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.integration;

import br.com.unimedceara.portalcomunicacao.configuration.properties.ZimbraProperties;
import br.com.unimedceara.portalcomunicacao.infrastructure.integration.client.IdentityProviderClient;
import br.com.unimedceara.portalcomunicacao.infrastructure.integration.client.IdentityValidationRequest;
import br.com.unimedceara.portalcomunicacao.infrastructure.integration.client.IdentityValidationResult;
import br.com.unimedceara.portalcomunicacao.infrastructure.integration.exception.IntegrationException;
import br.com.unimedceara.portalcomunicacao.infrastructure.integration.exception.IntegrationUnavailableException;
import br.com.unimedceara.portalcomunicacao.infrastructure.integration.executor.IntegrationHttpExecutor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

/**
 * Cliente Zimbra para autenticação e validação de identidade (FT-AUTH).
 */
@Component
public class ZimbraIdentityProviderClient implements IdentityProviderClient {

    private final RestClient restClient;
    private final ZimbraProperties zimbraProperties;
    private final IntegrationHttpExecutor integrationHttpExecutor;

    public ZimbraIdentityProviderClient(
            RestClient restClient,
            ZimbraProperties zimbraProperties,
            IntegrationHttpExecutor integrationHttpExecutor) {
        this.restClient = restClient;
        this.zimbraProperties = zimbraProperties;
        this.integrationHttpExecutor = integrationHttpExecutor;
    }

    @Override
    public URI buildAuthorizationUrl(String state, String callbackUrl) {
        return UriComponentsBuilder.fromUriString(zimbraProperties.authUrl())
                .queryParam("state", state)
                .queryParam("redirect_uri", callbackUrl)
                .build(true)
                .toUri();
    }

    @Override
    public IdentityValidationResult validateIdentity(IdentityValidationRequest request) {
        return integrationHttpExecutor.execute(() -> callValidateEndpoint(request.validationToken()));
    }

    private IdentityValidationResult callValidateEndpoint(String validationToken) {
        try {
            ZimbraValidationResponse response = restClient.post()
                    .uri(zimbraProperties.validateUrl())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("token", validationToken))
                    .retrieve()
                    .body(ZimbraValidationResponse.class);

            if (response == null
                    || isBlank(response.email())
                    || isBlank(response.displayName())
                    || isBlank(response.zimbraId())) {
                throw new IntegrationException("Invalid identity response from Zimbra");
            }

            return new IdentityValidationResult(response.email(), response.displayName(), response.zimbraId());
        } catch (IntegrationException ex) {
            throw ex;
        } catch (RestClientException ex) {
            throw new IntegrationUnavailableException("Zimbra identity provider unavailable", ex);
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private record ZimbraValidationResponse(String email, String displayName, String zimbraId) {
    }
}
