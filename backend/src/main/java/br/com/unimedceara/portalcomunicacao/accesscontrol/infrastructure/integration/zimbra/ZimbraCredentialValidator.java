package br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.integration.zimbra;

import br.com.unimedceara.portalcomunicacao.accesscontrol.application.port.IdentityCredentialValidator;
import br.com.unimedceara.portalcomunicacao.infrastructure.integration.client.IdentityValidationResult;
import br.com.unimedceara.portalcomunicacao.infrastructure.integration.exception.IntegrationUnavailableException;
import br.com.unimedceara.portalcomunicacao.shared.exception.UnauthorizedException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Validação de credenciais Zimbra — IMAP, SMTP (fallback) e SOAP (fallback / identidade).
 */
@Service
@Profile("!test")
public class ZimbraCredentialValidator implements IdentityCredentialValidator {

    private static final String UNAUTHORIZED_MESSAGE = "Autenticação não realizada";

    private final List<ZimbraMailAuthenticationStrategy> mailStrategies;
    private final ZimbraSoapIdentityResolver soapIdentityResolver;

    public ZimbraCredentialValidator(
            ZimbraImapAuthenticationStrategy imapStrategy,
            ZimbraSmtpAuthenticationStrategy smtpStrategy,
            ZimbraSoapIdentityResolver soapIdentityResolver) {
        this.mailStrategies = List.of(imapStrategy, smtpStrategy);
        this.soapIdentityResolver = soapIdentityResolver;
    }

    @Override
    public IdentityValidationResult validateCredentials(String email, String password) {
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            throw new UnauthorizedException(UNAUTHORIZED_MESSAGE);
        }

        ZimbraIntegrationDiagnostic.logFlowSummary(
                "validate_credentials_start", "emailDomain=" + emailDomain(email));

        boolean mailAuthenticated = false;
        RuntimeException lastMailError = null;

        for (int index = 0; index < mailStrategies.size(); index++) {
            ZimbraMailAuthenticationStrategy strategy = mailStrategies.get(index);
            ZimbraIntegrationDiagnostic.AttemptOrder order = index == 0
                    ? ZimbraIntegrationDiagnostic.AttemptOrder.PRIMARY
                    : ZimbraIntegrationDiagnostic.AttemptOrder.FALLBACK;
            String strategyLabel = strategy instanceof ZimbraImapAuthenticationStrategy ? "IMAP" : "SMTP";
            ZimbraIntegrationDiagnostic.logFlowSummary(
                    "mail_strategy_dispatch", "strategy=" + strategyLabel + " attemptOrder=" + order);

            try {
                if (strategy.tryAuthenticate(email, password)) {
                    mailAuthenticated = true;
                    ZimbraIntegrationDiagnostic.logFlowSummary(
                            "mail_strategy_success", "strategy=" + strategyLabel);
                    break;
                }
                ZimbraIntegrationDiagnostic.logFlowSummary(
                        "mail_strategy_rejected", "strategy=" + strategyLabel + " reason=invalid_credentials");
            } catch (ZimbraIntegrationException ex) {
                lastMailError = ex;
                ZimbraIntegrationDiagnostic.logFlowSummary(
                        "mail_strategy_failed",
                        "strategy=" + strategyLabel + " outcome="
                                + ZimbraIntegrationDiagnostic.classify(ex));
            }
        }

        if (mailAuthenticated) {
            ZimbraIntegrationDiagnostic.logFlowSummary(
                    "soap_identity_after_mail", "resolving identity via SOAP after successful mail auth");
            return soapIdentityResolver.resolveAfterMailAuth(email, password);
        }

        ZimbraIntegrationDiagnostic.logFlowSummary(
                "soap_fallback_auth", "mail strategies failed; attempting SOAP AuthRequest with password");
        try {
            return soapIdentityResolver.resolveWithSoapAuth(
                    email, password, ZimbraIntegrationDiagnostic.AttemptOrder.FALLBACK);
        } catch (UnauthorizedException | IntegrationUnavailableException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            if (lastMailError != null) {
                ZimbraIntegrationDiagnostic.logFlowSummary(
                        "integration_unavailable",
                        "propagating last mail error causeType="
                                + lastMailError.getClass().getName());
                throw new IntegrationUnavailableException("Zimbra indisponível", lastMailError);
            }
            throw new UnauthorizedException(UNAUTHORIZED_MESSAGE);
        }
    }

    @Override
    public IdentityValidationResult validateOpaqueToken(String opaqueToken) {
        if (opaqueToken == null || opaqueToken.isBlank()) {
            throw new UnauthorizedException(UNAUTHORIZED_MESSAGE);
        }
        ZimbraIntegrationDiagnostic.logFlowSummary("validate_opaque_token", "callback token validation via SOAP");
        try {
            return soapIdentityResolver.resolveByAuthToken(opaqueToken);
        } catch (UnauthorizedException | IntegrationUnavailableException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            throw new UnauthorizedException(UNAUTHORIZED_MESSAGE);
        }
    }

    private static String emailDomain(String email) {
        int at = email.indexOf('@');
        return at >= 0 ? email.substring(at + 1) : "unknown";
    }
}
