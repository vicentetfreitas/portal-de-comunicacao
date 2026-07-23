package br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.integration.zimbra;

import br.com.unimedceara.portalcomunicacao.infrastructure.logging.LoggingConstants;
import br.com.unimedceara.portalcomunicacao.infrastructure.logging.MdcUtils;
import jakarta.mail.AuthenticationFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.ResourceAccessException;

import javax.net.ssl.SSLException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 * Instrumentação diagnóstica da integração Zimbra (FT-AUTH). Não altera fluxo funcional.
 */
final class ZimbraIntegrationDiagnostic {

    static final String LOGGER_NAME =
            "br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.integration.zimbra.diagnostic";

    private static final Logger log = LoggerFactory.getLogger(LOGGER_NAME);

    private ZimbraIntegrationDiagnostic() {
    }

    enum Strategy {
        IMAP,
        SMTP,
        SOAP
    }

    enum AttemptOrder {
        PRIMARY,
        FALLBACK,
        IDENTITY_AFTER_MAIL,
        CALLBACK_TOKEN
    }

    enum AttemptOutcome {
        SUCCESS,
        AUTH_FAILURE,
        INTEGRATION_UNAVAILABLE,
        TIMEOUT,
        SSL_TLS,
        DNS,
        CONNECTION,
        UNEXPECTED
    }

    record AttemptContext(
            Strategy strategy,
            AttemptOrder attemptOrder,
            String host,
            int port,
            String protocol,
            boolean ssl,
            boolean startTls,
            int timeoutMs) {
    }

    static void logAttemptStart(AttemptContext context) {
        log.info(
                "zimbra_auth_diagnostic event=attempt_start correlationId={} strategy={} attemptOrder={} "
                        + "host={} port={} protocol={} ssl={} startTls={} timeoutMs={}",
                correlationId(),
                context.strategy(),
                context.attemptOrder(),
                context.host(),
                context.port(),
                context.protocol(),
                context.ssl(),
                context.startTls(),
                context.timeoutMs());
    }

    static void logAttemptEnd(AttemptContext context, long durationMs, AttemptOutcome outcome) {
        log.info(
                "zimbra_auth_diagnostic event=attempt_end correlationId={} strategy={} attemptOrder={} "
                        + "host={} port={} protocol={} durationMs={} outcome={}",
                correlationId(),
                context.strategy(),
                context.attemptOrder(),
                context.host(),
                context.port(),
                context.protocol(),
                durationMs,
                outcome);
    }

    static void logException(AttemptContext context, long durationMs, Throwable throwable) {
        AttemptOutcome outcome = classify(throwable);
        logAttemptEnd(context, durationMs, outcome);
        Throwable root = rootCause(throwable);
        log.error(
                "zimbra_auth_diagnostic event=attempt_error correlationId={} strategy={} attemptOrder={} "
                        + "host={} port={} protocol={} durationMs={} outcome={} exceptionType={} "
                        + "exceptionMessage={} rootCauseType={} rootCauseMessage={}",
                correlationId(),
                context.strategy(),
                context.attemptOrder(),
                context.host(),
                context.port(),
                context.protocol(),
                durationMs,
                outcome,
                throwable.getClass().getName(),
                throwable.getMessage(),
                root.getClass().getName(),
                root.getMessage(),
                throwable);
    }

    static void logAuthFailure(AttemptContext context, long durationMs, String detail) {
        logAttemptEnd(context, durationMs, AttemptOutcome.AUTH_FAILURE);
        log.info(
                "zimbra_auth_diagnostic event=auth_rejected correlationId={} strategy={} attemptOrder={} "
                        + "host={} port={} detail={}",
                correlationId(),
                context.strategy(),
                context.attemptOrder(),
                context.host(),
                context.port(),
                detail);
    }

    static void logFlowSummary(String phase, String detail) {
        log.info(
                "zimbra_auth_diagnostic event=flow correlationId={} phase={} detail={}",
                correlationId(),
                phase,
                detail);
    }

    static AttemptOutcome classify(Throwable throwable) {
        if (throwable == null) {
            return AttemptOutcome.UNEXPECTED;
        }
        if (throwable instanceof AuthenticationFailedException) {
            return AttemptOutcome.AUTH_FAILURE;
        }
        Throwable root = rootCause(throwable);
        if (root instanceof UnknownHostException) {
            return AttemptOutcome.DNS;
        }
        if (root instanceof SocketTimeoutException) {
            return AttemptOutcome.TIMEOUT;
        }
        if (root instanceof SSLException || hasCauseType(throwable, SSLException.class)) {
            return AttemptOutcome.SSL_TLS;
        }
        if (root instanceof ConnectException || hasCauseType(throwable, ConnectException.class)) {
            return AttemptOutcome.CONNECTION;
        }
        if (throwable instanceof ResourceAccessException) {
            AttemptOutcome nested = classify(root);
            return nested != AttemptOutcome.UNEXPECTED ? nested : AttemptOutcome.INTEGRATION_UNAVAILABLE;
        }
        String message = Objects.toString(root.getMessage(), "").toLowerCase();
        if (message.contains("timed out") || message.contains("timeout")) {
            return AttemptOutcome.TIMEOUT;
        }
        if (message.contains("ssl") || message.contains("tls") || message.contains("certificate")) {
            return AttemptOutcome.SSL_TLS;
        }
        if (message.contains("unknown host") || message.contains("nodename nor servname")) {
            return AttemptOutcome.DNS;
        }
        if (message.contains("connection refused") || message.contains("connect")) {
            return AttemptOutcome.CONNECTION;
        }
        return AttemptOutcome.UNEXPECTED;
    }

    private static boolean hasCauseType(Throwable throwable, Class<? extends Throwable> type) {
        Throwable current = throwable;
        while (current != null) {
            if (type.isInstance(current)) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }

    private static Throwable rootCause(Throwable throwable) {
        Throwable root = throwable;
        while (root.getCause() != null && root.getCause() != root) {
            root = root.getCause();
        }
        return root;
    }

    private static String correlationId() {
        String fromMdc = MdcUtils.get(LoggingConstants.MDC_CORRELATION_ID);
        return fromMdc != null && !fromMdc.isBlank() ? fromMdc : "unavailable";
    }
}
