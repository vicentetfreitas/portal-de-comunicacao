package br.com.unimedceara.portalcomunicacao.infrastructure.integration.executor;

import br.com.unimedceara.portalcomunicacao.infrastructure.integration.exception.IntegrationException;
import br.com.unimedceara.portalcomunicacao.infrastructure.integration.exception.IntegrationUnavailableException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

/**
 * Executor resiliente para operações HTTP de integração externa.
 */
@Component
public class IntegrationHttpExecutor {

    private static final String UNAVAILABLE_MESSAGE = "External integration is temporarily unavailable";
    private static final String EXECUTION_FAILED_MESSAGE = "External integration call failed";

    private final CircuitBreaker circuitBreaker;
    private final Retry retry;

    /**
     * Constrói o executor com circuit breaker e retry configurados.
     *
     * @param circuitBreaker circuit breaker de integração
     * @param retry          retry de integração
     */
    public IntegrationHttpExecutor(CircuitBreaker circuitBreaker, Retry retry) {
        this.circuitBreaker = circuitBreaker;
        this.retry = retry;
    }

    /**
     * Executa uma operação de integração com retry e circuit breaker.
     *
     * @param operation operação a executar
     * @param <T>       tipo do resultado
     * @return resultado da operação
     */
    public <T> T execute(Supplier<T> operation) {
        Supplier<T> decorated = CircuitBreaker.decorateSupplier(circuitBreaker, operation);
        decorated = Retry.decorateSupplier(retry, decorated);
        try {
            return decorated.get();
        } catch (CallNotPermittedException ex) {
            throw new IntegrationUnavailableException(UNAVAILABLE_MESSAGE, ex);
        } catch (IntegrationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IntegrationException(EXECUTION_FAILED_MESSAGE, ex);
        }
    }
}
