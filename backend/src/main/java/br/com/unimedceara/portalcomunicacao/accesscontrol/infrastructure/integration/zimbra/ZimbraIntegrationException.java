package br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.integration.zimbra;

/**
 * Erro técnico na integração Zimbra (camada de infraestrutura).
 */
public class ZimbraIntegrationException extends RuntimeException {

    public ZimbraIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
