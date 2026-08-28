package br.com.unimedceara.portalcomunicacao.accesscontrol.acceptance;

import br.com.unimedceara.portalcomunicacao.accesscontrol.application.port.IdentityCredentialValidator;
import br.com.unimedceara.portalcomunicacao.infrastructure.integration.client.IdentityProviderClient;
import br.com.unimedceara.portalcomunicacao.infrastructure.integration.client.IdentityValidationRequest;
import br.com.unimedceara.portalcomunicacao.infrastructure.integration.client.IdentityValidationResult;
import br.com.unimedceara.portalcomunicacao.infrastructure.integration.exception.IntegrationUnavailableException;
import br.com.unimedceara.portalcomunicacao.shared.exception.UnauthorizedException;
import br.com.unimedceara.portalcomunicacao.support.data.IntegrationTestUniqueData;

import java.net.URI;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * Provedor de identidade configurável para testes de aceite FT-AUTH.
 */
public class TestIdentityProviderClient implements IdentityProviderClient, IdentityCredentialValidator {

    public static final String VALID_TOKEN = "valid-callback-token";
    public static final String INVALID_CREDENTIALS_TOKEN = "invalid-credentials-token";
    public static final String ZIMBRA_UNAVAILABLE_TOKEN = "zimbra-unavailable-token";
    public static final String INACTIVE_COLABORADOR_TOKEN = "inactive-colaborador-token";

    /**
     * E-mail do usuário válido resolvido por {@link #VALID_TOKEN}. Único por execução da JVM
     * ({@link IntegrationTestUniqueData}) e estável dentro dela — não depende de nenhuma identidade
     * pré-existente no schema compartilhado (Oracle TST, sem limpeza automática — DEC-DB-023).
     * Estável para poder ser registrado como {@code session-administrator-emails} via
     * {@code @DynamicPropertySource}.
     */
    public static final String DEFAULT_VALID_TOKEN_EMAIL = IntegrationTestUniqueData.colaboradorEmail("valid-token");

    private final AtomicInteger validationCallCount = new AtomicInteger();

    private Function<String, IdentityValidationResult> validationBehavior = this::defaultValidation;

    private boolean authorizationUnavailable;

    private String validTokenEmail = DEFAULT_VALID_TOKEN_EMAIL;

    @Override
    public IdentityValidationResult validateIdentity(IdentityValidationRequest request) {
        return validateOpaqueToken(request.validationToken());
    }

    @Override
    public IdentityValidationResult validateOpaqueToken(String opaqueToken) {
        validationCallCount.incrementAndGet();
        return validationBehavior.apply(opaqueToken);
    }

    @Override
    public IdentityValidationResult validateCredentials(String email, String password) {
        validationCallCount.incrementAndGet();
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            throw new UnauthorizedException("Autenticação não realizada");
        }
        if ("invalid@unimedceara.com.br".equalsIgnoreCase(email)) {
            throw new UnauthorizedException("Credenciais inválidas");
        }
        if ("inactive@unimedceara.com.br".equalsIgnoreCase(email)) {
            return new IdentityValidationResult(
                    "inactive@unimedceara.com.br",
                    "Colaborador Inativo",
                    "zimbra-id-inactive");
        }
        if ("unavailable@unimedceara.com.br".equalsIgnoreCase(email)) {
            throw new IntegrationUnavailableException("Zimbra identity provider unavailable");
        }
        return new IdentityValidationResult(
                email,
                "Colaborador Teste",
                "zimbra-" + email);
    }

    @Override
    public URI buildAuthorizationUrl(String state, String callbackUrl) {
        if (authorizationUnavailable) {
            throw new IntegrationUnavailableException("Zimbra identity provider unavailable");
        }
        return URI.create("http://localhost:9000/auth?state=" + state + "&callback=" + callbackUrl);
    }

    public void reset() {
        validationCallCount.set(0);
        validationBehavior = this::defaultValidation;
        authorizationUnavailable = false;
        validTokenEmail = DEFAULT_VALID_TOKEN_EMAIL;
    }

    /**
     * E-mail que {@link #VALID_TOKEN} resolve na validação padrão. Use para as asserções
     * ({@code $.data.email}) em vez de um literal.
     */
    public String validTokenEmail() {
        return validTokenEmail;
    }

    public void setAuthorizationUnavailable(boolean authorizationUnavailable) {
        this.authorizationUnavailable = authorizationUnavailable;
    }

    public void setValidationBehavior(Function<String, IdentityValidationResult> validationBehavior) {
        this.validationBehavior = validationBehavior;
    }

    public int getValidationCallCount() {
        return validationCallCount.get();
    }

    private IdentityValidationResult defaultValidation(String token) {
        if (VALID_TOKEN.equals(token)) {
            return new IdentityValidationResult(
                    validTokenEmail,
                    "Colaborador Teste",
                    "zimbra-" + validTokenEmail);
        }
        if (INVALID_CREDENTIALS_TOKEN.equals(token)) {
            throw new UnauthorizedException("Credenciais inválidas");
        }
        if (ZIMBRA_UNAVAILABLE_TOKEN.equals(token)) {
            throw new IntegrationUnavailableException("Zimbra identity provider unavailable");
        }
        if (INACTIVE_COLABORADOR_TOKEN.equals(token)) {
            return new IdentityValidationResult(
                    "inactive@unimedceara.com.br",
                    "Colaborador Inativo",
                    "zimbra-id-inactive");
        }
        throw new UnauthorizedException("Autenticação não realizada");
    }
}
