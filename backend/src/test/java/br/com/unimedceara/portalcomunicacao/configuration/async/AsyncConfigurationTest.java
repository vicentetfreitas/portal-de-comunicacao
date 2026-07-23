package br.com.unimedceara.portalcomunicacao.configuration.async;

import br.com.unimedceara.portalcomunicacao.support.annotation.PlatformFoundationSliceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

import static org.assertj.core.api.Assertions.assertThat;

@PlatformFoundationSliceTest
class AsyncConfigurationTest {

    @Autowired
    @Qualifier(AsyncConfiguration.APPLICATION_TASK_EXECUTOR)
    private Executor applicationTaskExecutor;

    @Test
    void shouldRegisterApplicationTaskExecutor() {
        assertThat(applicationTaskExecutor).isInstanceOf(ThreadPoolTaskExecutor.class);

        ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) applicationTaskExecutor;
        assertThat(executor.getCorePoolSize()).isEqualTo(2);
        assertThat(executor.getMaxPoolSize()).isEqualTo(4);
        assertThat(executor.getQueueCapacity()).isEqualTo(50);
        assertThat(executor.getThreadNamePrefix()).isEqualTo("app-async-");
    }
}
