package br.com.unimedceara.portalcomunicacao.infrastructure.integration.client;

/**
 * Contrato abstrato para validação de identidade no provedor externo (Zimbra).
 * Implementação concreta fornecida em FT-AUTH.
 */
public interface IdentityProviderClient {

    /**
     * Valida a identidade do usuário junto ao provedor externo.
     *
     * @param request dados de validação retornados pelo callback de autenticação
     * @return identidade mínima autenticada
     */
    IdentityValidationResult validateIdentity(IdentityValidationRequest request);

    /**
     * Constrói a URL de redirecionamento para autenticação no provedor externo.
     *
     * @param state       token anti-CSRF do fluxo OAuth
     * @param callbackUrl URL de callback registrada no provedor
     * @return URI de redirecionamento ao provedor
     */
    java.net.URI buildAuthorizationUrl(String state, String callbackUrl);
}
