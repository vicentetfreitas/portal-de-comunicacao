package br.com.unimedceara.portalcomunicacao.infrastructure.integration.config;

import br.com.unimedceara.portalcomunicacao.configuration.properties.ZimbraProperties;
import br.com.unimedceara.portalcomunicacao.infrastructure.integration.interceptor.CorrelationIdInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;

/**
 * Configuração centralizada do cliente HTTP para integrações externas.
 */
@Configuration
public class RestClientConfiguration {

    /**
     * Bean RestClient com timeout Zimbra ({@code application.zimbra.timeout-ms}) e propagação de Correlation ID.
     *
     * @param zimbraProperties         propriedades de timeout Zimbra (RNF-AUTH-006)
     * @param correlationIdInterceptor interceptor de Correlation ID
     * @return cliente HTTP configurado
     */
    @Bean
    public RestClient restClient(
            ZimbraProperties zimbraProperties,
            CorrelationIdInterceptor correlationIdInterceptor) {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(zimbraProperties.timeoutMs()))
                .build();
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(Duration.ofMillis(zimbraProperties.timeoutMs()));

        return RestClient.builder()
                .requestFactory(requestFactory)
                .requestInterceptor(correlationIdInterceptor)
                .build();
    }
}
