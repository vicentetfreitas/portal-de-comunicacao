package br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.integration.zimbra;

import br.com.unimedceara.portalcomunicacao.configuration.properties.ZimbraProperties;
import br.com.unimedceara.portalcomunicacao.infrastructure.integration.client.IdentityValidationResult;
import br.com.unimedceara.portalcomunicacao.infrastructure.integration.exception.IntegrationUnavailableException;
import br.com.unimedceara.portalcomunicacao.infrastructure.integration.executor.IntegrationHttpExecutor;
import br.com.unimedceara.portalcomunicacao.shared.exception.UnauthorizedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.mock.http.client.MockClientHttpRequest;
import org.springframework.mock.http.client.MockClientHttpResponse;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ZimbraSoapIdentityResolverTest {

    private static final String SOAP_URL = "https://mail.example.com/service/soap";
    private static final String EMAIL = "user@example.com";
    private static final String PASSWORD = "invalid-password";

    private static final String AUTH_FAILED_FAULT = """
            <soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope">\
            <soap:Header><context xmlns="urn:zimbra"/></soap:Header>\
            <soap:Body><soap:Fault>\
            <soap:Code><soap:Value>soap:Sender</soap:Value></soap:Code>\
            <soap:Reason><soap:Text>authentication failed for [user@example.com]</soap:Text></soap:Reason>\
            <soap:Detail><Error xmlns="urn:zimbra">\
            <Code>account.AUTH_FAILED</Code><Trace>qtp-test</Trace>\
            </Error></soap:Detail></soap:Fault></soap:Body></soap:Envelope>
            """;

    private static final String SERVICE_FAILURE_FAULT = """
            <soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope">\
            <soap:Body><soap:Fault>\
            <soap:Code><soap:Value>soap:Receiver</soap:Value></soap:Code>\
            <soap:Reason><soap:Text>invalid request: No SOAP body</soap:Text></soap:Reason>\
            <soap:Detail><Error xmlns="urn:zimbra">\
            <Code>service.FAILURE</Code>\
            </Error></soap:Detail></soap:Fault></soap:Body></soap:Envelope>
            """;

    private static final String AUTH_RESPONSE = """
            <soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope">\
            <soap:Body>\
            <AuthResponse xmlns="urn:zimbraAccount" id="zimbra-id-001">\
            <cn>Test User</cn>\
            </AuthResponse>\
            </soap:Body></soap:Envelope>
            """;

    @Test
    void shouldRejectAuthFailedSoapFaultAsUnauthorized() {
        ZimbraSoapIdentityResolver resolver = resolverResponding(HttpStatus.INTERNAL_SERVER_ERROR, AUTH_FAILED_FAULT);

        assertThatThrownBy(() -> resolver.resolveWithSoapAuth(EMAIL, PASSWORD))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("Autenticação não realizada")
                .extracting("errorCode")
                .isEqualTo(UnauthorizedException.ERROR_CODE);
    }

    @Test
    void shouldKeepInfrastructureTimeoutAsUnavailable() {
        RestClient restClient = RestClient.builder()
                .requestFactory((uri, method) -> new MockClientHttpRequest(method, uri) {
                    @Override
                    protected ClientHttpResponse executeInternal() throws IOException {
                        throw new SocketTimeoutException("Read timed out");
                    }
                })
                .build();
        ZimbraSoapIdentityResolver resolver = newResolver(restClient);

        assertThatThrownBy(() -> resolver.resolveWithSoapAuth(EMAIL, PASSWORD))
                .isInstanceOf(IntegrationUnavailableException.class)
                .hasMessage("Zimbra SOAP indisponível")
                .cause()
                .isInstanceOf(ResourceAccessException.class);
    }

    @Test
    void shouldKeepHttp503AsUnavailable() {
        ZimbraSoapIdentityResolver resolver = resolverResponding(
                HttpStatus.SERVICE_UNAVAILABLE, SERVICE_FAILURE_FAULT);

        assertThatThrownBy(() -> resolver.resolveWithSoapAuth(EMAIL, PASSWORD))
                .isInstanceOf(IntegrationUnavailableException.class)
                .hasMessage("Zimbra SOAP indisponível");
    }

    @Test
    void shouldNotTreatUnknownSoapFaultAsUnauthorized() {
        ZimbraSoapIdentityResolver resolver = resolverResponding(
                HttpStatus.INTERNAL_SERVER_ERROR, SERVICE_FAILURE_FAULT);

        assertThatThrownBy(() -> resolver.resolveWithSoapAuth(EMAIL, PASSWORD))
                .isInstanceOf(IntegrationUnavailableException.class)
                .hasMessage("Zimbra SOAP indisponível")
                .isNotInstanceOf(UnauthorizedException.class);
    }

    @Test
    void shouldNotTreatAuthFailedTextOutsideCodeElementAsUnauthorized() {
        String misleadingFault = """
                <soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope">\
                <soap:Body><soap:Fault>\
                <soap:Reason><soap:Text>see AUTH_FAILED documentation</soap:Text></soap:Reason>\
                <soap:Detail><Error xmlns="urn:zimbra"><Code>service.FAILURE</Code></Error></soap:Detail>\
                </soap:Fault></soap:Body></soap:Envelope>
                """;
        ZimbraSoapIdentityResolver resolver = resolverResponding(
                HttpStatus.INTERNAL_SERVER_ERROR, misleadingFault);

        assertThatThrownBy(() -> resolver.resolveWithSoapAuth(EMAIL, PASSWORD))
                .isInstanceOf(IntegrationUnavailableException.class);
        assertThat(ZimbraSoapIdentityResolver.isSoapAuthRejected(misleadingFault)).isFalse();
        assertThat(ZimbraSoapIdentityResolver.isSoapAuthRejected(AUTH_FAILED_FAULT)).isTrue();
    }

    @Test
    void shouldResolveIdentityFromSuccessfulAuthResponse() {
        ZimbraSoapIdentityResolver resolver = resolverResponding(HttpStatus.OK, AUTH_RESPONSE);

        IdentityValidationResult result = resolver.resolveWithSoapAuth(EMAIL, PASSWORD);

        assertThat(result.email()).isEqualTo(EMAIL);
        assertThat(result.displayName()).isEqualTo("Test User");
        assertThat(result.zimbraId()).isEqualTo("zimbra-id-001");
    }

    @Test
    void shouldKeepHttp503EvenWhenBodyContainsAuthFailed() {
        ZimbraSoapIdentityResolver resolver = resolverResponding(
                HttpStatus.SERVICE_UNAVAILABLE, AUTH_FAILED_FAULT);

        assertThatThrownBy(() -> resolver.resolveWithSoapAuth(EMAIL, PASSWORD))
                .isInstanceOf(IntegrationUnavailableException.class)
                .isNotInstanceOf(UnauthorizedException.class);
    }

    private static ZimbraSoapIdentityResolver resolverResponding(HttpStatus status, String body) {
        RestClient restClient = RestClient.builder()
                .requestFactory((uri, method) -> new MockClientHttpRequest(method, uri) {
                    @Override
                    protected ClientHttpResponse executeInternal() {
                        byte[] payload = body.getBytes(StandardCharsets.UTF_8);
                        MockClientHttpResponse response = new MockClientHttpResponse(payload, status);
                        response.getHeaders().setContentType(MediaType.TEXT_XML);
                        return response;
                    }
                })
                .build();
        return newResolver(restClient);
    }

    private static ZimbraSoapIdentityResolver newResolver(RestClient restClient) {
        return new ZimbraSoapIdentityResolver(restClient, zimbraProperties(), passthroughExecutor());
    }

    private static ZimbraProperties zimbraProperties() {
        return new ZimbraProperties(
                "http://localhost:9000/auth",
                "imap.example.com",
                993,
                true,
                "smtp.example.com",
                587,
                false,
                true,
                SOAP_URL,
                8000);
    }

    private static IntegrationHttpExecutor passthroughExecutor() {
        CircuitBreaker circuitBreaker = CircuitBreaker.of(
                "zimbra-soap-mapping-test",
                CircuitBreakerConfig.custom()
                        .failureRateThreshold(100)
                        .waitDurationInOpenState(Duration.ofMinutes(1))
                        .slidingWindowSize(10)
                        .minimumNumberOfCalls(10)
                        .ignoreExceptions(UnauthorizedException.class)
                        .build());
        Retry retry = Retry.of(
                "zimbra-soap-mapping-test",
                RetryConfig.custom().maxAttempts(1).build());
        return new IntegrationHttpExecutor(circuitBreaker, retry);
    }
}
