package br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.integration.zimbra;

import br.com.unimedceara.portalcomunicacao.configuration.properties.ZimbraProperties;
import jakarta.mail.AuthenticationFailedException;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Store;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * Valida credenciais via IMAP (estratégia primária — legado {@code ZimbraAuth.php}).
 */
@Component
@Profile("!test")
public class ZimbraImapAuthenticationStrategy implements ZimbraMailAuthenticationStrategy {

    private final ZimbraProperties zimbraProperties;

    public ZimbraImapAuthenticationStrategy(ZimbraProperties zimbraProperties) {
        this.zimbraProperties = zimbraProperties;
    }

    @Override
    public boolean tryAuthenticate(String email, String password) {
        boolean ssl = zimbraProperties.imapSsl();
        String protocol = ssl ? "imaps" : "imap";
        ZimbraIntegrationDiagnostic.AttemptContext context = new ZimbraIntegrationDiagnostic.AttemptContext(
                ZimbraIntegrationDiagnostic.Strategy.IMAP,
                ZimbraIntegrationDiagnostic.AttemptOrder.PRIMARY,
                zimbraProperties.imapHost(),
                zimbraProperties.imapPort(),
                protocol,
                ssl,
                false,
                zimbraProperties.timeoutMs());

        long startedAt = System.nanoTime();
        ZimbraIntegrationDiagnostic.logAttemptStart(context);

        Properties mailProps = new Properties();
        mailProps.put("mail.store.protocol", protocol);
        mailProps.put("mail." + protocol + ".connectiontimeout", String.valueOf(zimbraProperties.timeoutMs()));
        mailProps.put("mail." + protocol + ".timeout", String.valueOf(zimbraProperties.timeoutMs()));
        if (ssl) {
            mailProps.put("mail.imaps.ssl.trust", zimbraProperties.imapHost());
        }

        Session session = Session.getInstance(mailProps);
        try (Store store = session.getStore(protocol)) {
            store.connect(zimbraProperties.imapHost(), zimbraProperties.imapPort(), email, password);
            long durationMs = elapsedMs(startedAt);
            ZimbraIntegrationDiagnostic.logAttemptEnd(
                    context, durationMs, ZimbraIntegrationDiagnostic.AttemptOutcome.SUCCESS);
            return true;
        } catch (AuthenticationFailedException ex) {
            long durationMs = elapsedMs(startedAt);
            ZimbraIntegrationDiagnostic.logAuthFailure(
                    context, durationMs, "AuthenticationFailedException: credenciais recusadas pelo servidor IMAP");
            return false;
        } catch (MessagingException ex) {
            long durationMs = elapsedMs(startedAt);
            ZimbraIntegrationDiagnostic.logException(context, durationMs, ex);
            throw new ZimbraIntegrationException("Falha ao conectar ao IMAP Zimbra", ex);
        }
    }

    private static long elapsedMs(long startedAtNanos) {
        return (System.nanoTime() - startedAtNanos) / 1_000_000L;
    }
}
