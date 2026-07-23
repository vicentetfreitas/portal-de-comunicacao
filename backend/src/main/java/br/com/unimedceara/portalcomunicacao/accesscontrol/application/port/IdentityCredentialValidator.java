package br.com.unimedceara.portalcomunicacao.accesscontrol.application.port;

import br.com.unimedceara.portalcomunicacao.infrastructure.integration.client.IdentityValidationResult;

/**
 * Porta de aplicação para validação de identidade no provedor externo (FT-AUTH).
 * Nenhum detalhe de protocolo Zimbra deve vazar para camadas superiores.
 */
public interface IdentityCredentialValidator {

    /**
     * Valida e-mail corporativo e senha junto ao provedor de identidade.
     */
    IdentityValidationResult validateCredentials(String email, String password);

    /**
     * Valida retorno opaco do callback (compatibilidade com fluxos que entregam token).
     */
    IdentityValidationResult validateOpaqueToken(String opaqueToken);
}
