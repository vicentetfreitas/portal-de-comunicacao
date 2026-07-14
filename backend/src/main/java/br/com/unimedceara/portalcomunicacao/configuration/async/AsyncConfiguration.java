package br.com.unimedceara.portalcomunicacao.configuration.async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Configura a infraestrutura compartilhada para execução assíncrona.
 */
@Configuration
@EnableAsync
public class AsyncConfiguration {

    /**
     * Nome do executor assíncrono padrão da aplicação.
     */
    public static final String APPLICATION_TASK_EXECUTOR = "applicationTaskExecutor";

    private static final int CORE_POOL_SIZE = 2;
    private static final int MAX_POOL_SIZE = 4;
    private static final int QUEUE_CAPACITY = 50;
    private static final String THREAD_NAME_PREFIX = "app-async-";

    /**
     * Executor utilizado por operações assíncronas compartilhadas da aplicação.
     *
     * @return executor configurado
     */
    @Bean(name = APPLICATION_TASK_EXECUTOR)
    public Executor applicationTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setThreadNamePrefix(THREAD_NAME_PREFIX);
        executor.initialize();
        return executor;
    }
}
