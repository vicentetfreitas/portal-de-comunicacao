package br.com.unimedceara.portalcomunicacao.infrastructure.integration.executor;

import br.com.unimedceara.portalcomunicacao.infrastructure.integration.exception.IntegrationException;
import br.com.unimedceara.portalcomunicacao.infrastructure.integration.exception.IntegrationUnavailableException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.ResourceAccessException;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IntegrationHttpExecutorTest {

    @Test
    void shouldRetryOnTransientFailure() {
        IntegrationHttpExecutor executor = createExecutor(100, 100, 3);
        AtomicInteger attempts = new AtomicInteger();

        String result = executor.execute(() -> {
            if (attempts.incrementAndGet() < 3) {
                throw new ResourceAccessException("transient failure", new IOException("timeout"));
            }
            return "success";
        });

        assertThat(result).isEqualTo("success");
        assertThat(attempts).hasValue(3);
    }

    @Test
    void shouldOpenCircuitBreakerAfterFailureThreshold() {
        IntegrationHttpExecutor executor = createExecutor(50, 4, 1);
        AtomicInteger attempts = new AtomicInteger();

        for (int i = 0; i < 4; i++) {
            assertThatThrownBy(() -> executor.execute(() -> {
                attempts.incrementAndGet();
                throw new ResourceAccessException("persistent failure", new IOException("unavailable"));
            })).isInstanceOf(IntegrationException.class);
        }

        assertThatThrownBy(() -> executor.execute(() -> "should not execute"))
                .isInstanceOf(IntegrationUnavailableException.class);

        assertThat(attempts).hasValue(4);
    }

    private IntegrationHttpExecutor createExecutor(int failureThreshold, int windowSize, int maxAttempts) {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(failureThreshold)
                .waitDurationInOpenState(Duration.ofMinutes(1))
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(windowSize)
                .minimumNumberOfCalls(windowSize)
                .build();
        CircuitBreaker circuitBreaker = CircuitBreaker.of("test-integration", circuitBreakerConfig);

        RetryConfig retryConfig = RetryConfig.custom()
                .maxAttempts(maxAttempts)
                .retryExceptions(ResourceAccessException.class)
                .build();
        Retry retry = Retry.of("test-integration", retryConfig);

        return new IntegrationHttpExecutor(circuitBreaker, retry);
    }
}
