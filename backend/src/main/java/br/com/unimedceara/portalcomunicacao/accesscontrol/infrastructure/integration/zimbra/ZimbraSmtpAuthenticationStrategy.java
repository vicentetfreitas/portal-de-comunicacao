package br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.integration.zimbra;

import br.com.unimedceara.portalcomunicacao.configuration.properties.ZimbraProperties;
import jakarta.mail.AuthenticationFailedException;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * Valida credenciais via SMTP AUTH (fallback — legado).
 */
@Component
@Profile("!test")
public class ZimbraSmtpAuthenticationStrategy implements ZimbraMailAuthenticationStrategy {

    private final ZimbraProperties zimbraProperties;

    public ZimbraSmtpAuthenticationStrategy(ZimbraProperties zimbraProperties) {
        this.zimbraProperties = zimbraProperties;
    }

    @Override
    public boolean tryAuthenticate(String email, String password) {
        boolean ssl = zimbraProperties.smtpSsl();
        boolean startTls = zimbraProperties.smtpStartTls();
        ZimbraIntegrationDiagnostic.AttemptContext context = new ZimbraIntegrationDiagnostic.AttemptContext(
                ZimbraIntegrationDiagnostic.Strategy.SMTP,
                ZimbraIntegrationDiagnostic.AttemptOrder.FALLBACK,
                zimbraProperties.smtpHost(),
                zimbraProperties.smtpPort(),
                "smtp",
                ssl,
                startTls,
                zimbraProperties.timeoutMs());

        long startedAt = System.nanoTime();
        ZimbraIntegrationDiagnostic.logAttemptStart(context);

        Properties mailProps = new Properties();
        mailProps.put("mail.transport.protocol", "smtp");
        mailProps.put("mail.smtp.auth", "true");
        mailProps.put("mail.smtp.connectiontimeout", String.valueOf(zimbraProperties.timeoutMs()));
        mailProps.put("mail.smtp.timeout", String.valueOf(zimbraProperties.timeoutMs()));
        if (ssl) {
            mailProps.put("mail.smtp.ssl.enable", "true");
        }
        if (startTls) {
            mailProps.put("mail.smtp.starttls.enable", "true");
        }

        Session session = Session.getInstance(mailProps);
        try (Transport transport = session.getTransport("smtp")) {
            transport.connect(
                    zimbraProperties.smtpHost(),
                    zimbraProperties.smtpPort(),
                    email,
                    password);
            long durationMs = elapsedMs(startedAt);
            ZimbraIntegrationDiagnostic.logAttemptEnd(
                    context, durationMs, ZimbraIntegrationDiagnostic.AttemptOutcome.SUCCESS);
            return true;
        } catch (AuthenticationFailedException ex) {
            long durationMs = elapsedMs(startedAt);
            ZimbraIntegrationDiagnostic.logAuthFailure(
                    context, durationMs, "AuthenticationFailedException: credenciais recusadas pelo servidor SMTP");
            return false;
        } catch (MessagingException ex) {
            long durationMs = elapsedMs(startedAt);
            ZimbraIntegrationDiagnostic.logException(context, durationMs, ex);
            throw new ZimbraIntegrationException("Falha ao conectar ao SMTP Zimbra", ex);
        }
    }

    private static long elapsedMs(long startedAtNanos) {
        return (System.nanoTime() - startedAtNanos) / 1_000_000L;
    }
}
