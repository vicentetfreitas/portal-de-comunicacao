package br.com.unimedceara.portalcomunicacao.infrastructure.logging;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.core.Ordered;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class LoggingConfigurationTest {

    @Autowired
    private FilterRegistrationBean<CorrelationIdFilter> correlationIdFilterRegistration;

    @Test
    void shouldRegisterCorrelationIdFilter() {
        assertThat(correlationIdFilterRegistration).isNotNull();
        assertThat(correlationIdFilterRegistration.getFilter()).isInstanceOf(CorrelationIdFilter.class);
        assertThat(correlationIdFilterRegistration.getOrder()).isEqualTo(Ordered.HIGHEST_PRECEDENCE);
    }
}
