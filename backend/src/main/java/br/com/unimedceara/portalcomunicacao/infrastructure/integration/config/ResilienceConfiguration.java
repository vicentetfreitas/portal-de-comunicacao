package br.com.unimedceara.portalcomunicacao.infrastructure.integration.config;

import br.com.unimedceara.portalcomunicacao.configuration.properties.IntegrationProperties;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.ResourceAccessException;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

/**
 * Configuração de resiliência (retry e circuit breaker) para integrações HTTP.
 * Estratégia CD-S1A-004: Resilience4j.
 */
@Configuration
public class ResilienceConfiguration {

    /**
     * Nome lógico do circuit breaker de integração.
     */
    public static final String INTEGRATION_CIRCUIT_BREAKER = "integration";

    /**
     * Nome lógico do retry de integração.
     */
    public static final String INTEGRATION_RETRY = "integration";

    /**
     * Circuit breaker para chamadas de integração externa.
     *
     * @param integrationProperties propriedades de threshold
     * @return circuit breaker configurado
     */
    @Bean
    public CircuitBreaker integrationCircuitBreaker(IntegrationProperties integrationProperties) {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold(integrationProperties.circuitBreakerThreshold())
                .waitDurationInOpenState(Duration.ofSeconds(30))
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(10)
                .minimumNumberOfCalls(5)
                .build();
        return CircuitBreaker.of(INTEGRATION_CIRCUIT_BREAKER, config);
    }

    /**
     * Retry para falhas transitórias em integrações externas.
     *
     * @param integrationProperties propriedades de tentativas
     * @return retry configurado
     */
    @Bean
    public Retry integrationRetry(IntegrationProperties integrationProperties) {
        RetryConfig config = RetryConfig.custom()
                .maxAttempts(integrationProperties.maxRetryAttempts())
                .intervalFunction(IntervalFunction.ofExponentialBackoff(Duration.ofMillis(200), 2.0))
                .retryExceptions(
                        IOException.class,
                        TimeoutException.class,
                        ResourceAccessException.class)
                .build();
        return Retry.of(INTEGRATION_RETRY, config);
    }
}
