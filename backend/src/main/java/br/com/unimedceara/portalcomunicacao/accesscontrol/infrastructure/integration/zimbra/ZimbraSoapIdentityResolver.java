package br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.integration.zimbra;

import br.com.unimedceara.portalcomunicacao.configuration.properties.ZimbraProperties;
import br.com.unimedceara.portalcomunicacao.infrastructure.integration.client.IdentityValidationResult;
import br.com.unimedceara.portalcomunicacao.infrastructure.integration.exception.IntegrationUnavailableException;
import br.com.unimedceara.portalcomunicacao.shared.exception.UnauthorizedException;
import br.com.unimedceara.portalcomunicacao.infrastructure.integration.executor.IntegrationHttpExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Resolve identidade mínima via SOAP Zimbra ({@code AuthRequest} / atributos de conta).
 */
@Component
@Profile("!test")
public class ZimbraSoapIdentityResolver {

    private static final Pattern ZIMBRA_ID_PATTERN =
            Pattern.compile("id=\"([^\"]+)\"", Pattern.CASE_INSENSITIVE);
    private static final Pattern DISPLAY_NAME_PATTERN =
            Pattern.compile("<cn>([^<]+)</cn>", Pattern.CASE_INSENSITIVE);
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("name=\"([^\"]+@[^\"]+)\"", Pattern.CASE_INSENSITIVE);
    /**
     * Código SOAP Zimbra de rejeição de autenticação, no elemento {@code <Code>} do Fault.
     */
    private static final Pattern SOAP_AUTH_FAILED_CODE_PATTERN = Pattern.compile(
            "<(?:[A-Za-z_][\\w.-]*:)?Code\\s*>\\s*account\\.AUTH_FAILED\\s*</(?:[A-Za-z_][\\w.-]*:)?Code\\s*>",
            Pattern.CASE_INSENSITIVE);

    private final RestClient restClient;
    private final ZimbraProperties zimbraProperties;
    private final IntegrationHttpExecutor integrationHttpExecutor;

    public ZimbraSoapIdentityResolver(
            RestClient restClient,
            ZimbraProperties zimbraProperties,
            IntegrationHttpExecutor integrationHttpExecutor) {
        this.restClient = restClient;
        this.zimbraProperties = zimbraProperties;
        this.integrationHttpExecutor = integrationHttpExecutor;
    }

    /**
     * Autentica via SOAP e retorna identidade mínima.
     */
    public IdentityValidationResult resolveWithSoapAuth(String email, String password) {
        return resolveWithSoapAuth(email, password, ZimbraIntegrationDiagnostic.AttemptOrder.FALLBACK);
    }

    IdentityValidationResult resolveWithSoapAuth(
            String email, String password, ZimbraIntegrationDiagnostic.AttemptOrder attemptOrder) {
        return integrationHttpExecutor.execute(() -> executeSoapPasswordAuth(email, password, attemptOrder));
    }

    /**
     * Enriquece identidade após autenticação IMAP/SMTP bem-sucedida.
     */
    public IdentityValidationResult resolveAfterMailAuth(String email, String password) {
        try {
            return resolveWithSoapAuth(
                    email, password, ZimbraIntegrationDiagnostic.AttemptOrder.IDENTITY_AFTER_MAIL);
        } catch (RuntimeException ex) {
            ZimbraIntegrationDiagnostic.logFlowSummary(
                    "soap_identity_fallback_local",
                    "SOAP identity failed after mail auth; using email fallback. outcome="
                            + ZimbraIntegrationDiagnostic.classify(ex));
            String localPart = email.contains("@") ? email.substring(0, email.indexOf('@')) : email;
            return new IdentityValidationResult(email, localPart, email);
        }
    }

    /**
     * Valida token opaco de callback via {@code AuthRequest} com {@code authToken}.
     */
    public IdentityValidationResult resolveByAuthToken(String authToken) {
        return integrationHttpExecutor.execute(
                () -> executeSoapTokenAuth(authToken, ZimbraIntegrationDiagnostic.AttemptOrder.CALLBACK_TOKEN));
    }

    private IdentityValidationResult executeSoapPasswordAuth(
            String email, String password, ZimbraIntegrationDiagnostic.AttemptOrder attemptOrder) {
        ZimbraIntegrationDiagnostic.AttemptContext context = buildSoapContext(attemptOrder);
        long startedAt = System.nanoTime();
        ZimbraIntegrationDiagnostic.logAttemptStart(context);

        String envelope = """
                <?xml version="1.0" encoding="UTF-8"?>
                <soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope">
                  <soap:Body>
                    <AuthRequest xmlns="urn:zimbraAccount">
                      <account by="name">%s</account>
                      <password>%s</password>
                    </AuthRequest>
                  </soap:Body>
                </soap:Envelope>
                """.formatted(escapeXml(email), escapeXml(password));

        try {
            IdentityValidationResult result = parseAuthResponse(postSoap(envelope, context, startedAt), email);
            long durationMs = elapsedMs(startedAt);
            ZimbraIntegrationDiagnostic.logAttemptEnd(
                    context, durationMs, ZimbraIntegrationDiagnostic.AttemptOutcome.SUCCESS);
            return result;
        } catch (UnauthorizedException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            long durationMs = elapsedMs(startedAt);
            ZimbraIntegrationDiagnostic.logException(context, durationMs, ex);
            throw ex;
        }
    }

    private IdentityValidationResult executeSoapTokenAuth(
            String authToken, ZimbraIntegrationDiagnostic.AttemptOrder attemptOrder) {
        ZimbraIntegrationDiagnostic.AttemptContext context = buildSoapContext(attemptOrder);
        long startedAt = System.nanoTime();
        ZimbraIntegrationDiagnostic.logAttemptStart(context);

        String envelope = """
                <?xml version="1.0" encoding="UTF-8"?>
                <soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope">
                  <soap:Body>
                    <AuthRequest xmlns="urn:zimbraAccount">
                      <authToken>%s</authToken>
                    </AuthRequest>
                  </soap:Body>
                </soap:Envelope>
                """.formatted(escapeXml(authToken));

        try {
            String body = postSoap(envelope, context, startedAt);
            String email = extract(body, EMAIL_PATTERN, null);
            if (email == null || email.isBlank()) {
                long durationMs = elapsedMs(startedAt);
                ZimbraIntegrationDiagnostic.logAuthFailure(context, durationMs, "SOAP response without account email");
                throw new UnauthorizedException("Autenticação não realizada");
            }
            IdentityValidationResult result = parseAuthResponse(body, email);
            long durationMs = elapsedMs(startedAt);
            ZimbraIntegrationDiagnostic.logAttemptEnd(
                    context, durationMs, ZimbraIntegrationDiagnostic.AttemptOutcome.SUCCESS);
            return result;
        } catch (UnauthorizedException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            long durationMs = elapsedMs(startedAt);
            ZimbraIntegrationDiagnostic.logException(context, durationMs, ex);
            throw ex;
        }
    }

    private String postSoap(
            String envelope,
            ZimbraIntegrationDiagnostic.AttemptContext context,
            long startedAt) {
        try {
            String body = restClient.post()
                    .uri(zimbraProperties.soapUrl())
                    .contentType(MediaType.TEXT_XML)
                    .body(envelope)
                    .retrieve()
                    .body(String.class);
            return requireSuccessfulSoapBody(body, context, startedAt);
        } catch (RestClientResponseException ex) {
            throw mapSoapHttpError(ex, context, startedAt);
        } catch (RestClientException ex) {
            throw new IntegrationUnavailableException("Zimbra SOAP indisponível", ex);
        }
    }

    private String requireSuccessfulSoapBody(
            String body,
            ZimbraIntegrationDiagnostic.AttemptContext context,
            long startedAt) {
        if (body == null || body.isBlank()) {
            throw new ZimbraIntegrationException("Resposta SOAP vazia do Zimbra", null);
        }
        if (isSoapAuthRejected(body)) {
            throw soapAuthRejected(context, startedAt);
        }
        return body;
    }

    private RuntimeException mapSoapHttpError(
            RestClientResponseException ex,
            ZimbraIntegrationDiagnostic.AttemptContext context,
            long startedAt) {
        if (isRemoteUnavailable(ex.getStatusCode())) {
            return new IntegrationUnavailableException("Zimbra SOAP indisponível", ex);
        }
        if (isSoapAuthRejected(ex.getResponseBodyAsString())) {
            return soapAuthRejected(context, startedAt);
        }
        return new IntegrationUnavailableException("Zimbra SOAP indisponível", ex);
    }

    private static UnauthorizedException soapAuthRejected(
            ZimbraIntegrationDiagnostic.AttemptContext context, long startedAt) {
        long durationMs = elapsedMs(startedAt);
        ZimbraIntegrationDiagnostic.logAuthFailure(
                context, durationMs, "SOAP authentication rejected");
        return new UnauthorizedException("Autenticação não realizada");
    }

    /**
     * Reconhece rejeição de autenticação pelo elemento {@code <Code>account.AUTH_FAILED</Code>}
     * do SOAP Fault Zimbra, sem classificar ocorrências soltas do texto {@code AUTH_FAILED}.
     */
    static boolean isSoapAuthRejected(String soapBody) {
        if (soapBody == null || soapBody.isBlank()) {
            return false;
        }
        if (SOAP_AUTH_FAILED_CODE_PATTERN.matcher(soapBody).find()) {
            return true;
        }
        return soapBody.contains("authError") || soapBody.contains("INVALID_CREDENTIALS");
    }

    private static boolean isRemoteUnavailable(HttpStatusCode statusCode) {
        return statusCode.value() == HttpStatus.SERVICE_UNAVAILABLE.value();
    }

    private ZimbraIntegrationDiagnostic.AttemptContext buildSoapContext(
            ZimbraIntegrationDiagnostic.AttemptOrder attemptOrder) {
        URI uri = URI.create(zimbraProperties.soapUrl());
        int port = uri.getPort() > 0 ? uri.getPort() : ("https".equalsIgnoreCase(uri.getScheme()) ? 443 : 80);
        return new ZimbraIntegrationDiagnostic.AttemptContext(
                ZimbraIntegrationDiagnostic.Strategy.SOAP,
                attemptOrder,
                uri.getHost() != null ? uri.getHost() : zimbraProperties.soapUrl(),
                port,
                uri.getScheme() != null ? uri.getScheme() : "https",
                "https".equalsIgnoreCase(uri.getScheme()),
                false,
                zimbraProperties.timeoutMs());
    }

    private IdentityValidationResult parseAuthResponse(String body, String email) {
        String zimbraId = extract(body, ZIMBRA_ID_PATTERN, email);
        String displayName = extract(body, DISPLAY_NAME_PATTERN, zimbraId);
        return new IdentityValidationResult(email, displayName, zimbraId);
    }

    private static String extract(String body, Pattern pattern, String fallback) {
        Matcher matcher = pattern.matcher(body);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return fallback;
    }

    private static String escapeXml(String value) {
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    private static long elapsedMs(long startedAtNanos) {
        return (System.nanoTime() - startedAtNanos) / 1_000_000L;
    }
}
