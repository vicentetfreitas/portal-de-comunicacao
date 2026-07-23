package br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.integration.zimbra;

/**
 * Estratégia de autenticação de credenciais contra serviços de e-mail Zimbra (IMAP/SMTP).
 */
@FunctionalInterface
public interface ZimbraMailAuthenticationStrategy {

    /**
     * @return {@code true} se as credenciais foram aceitas pelo servidor
     */
    boolean tryAuthenticate(String email, String password);
}
