package br.com.unimedceara.portalcomunicacao.configuration.properties;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:pf-conf-test.properties")
class SecurityPropertiesTest {

    @Autowired
    private SecurityProperties securityProperties;

    @Test
    void shouldLoadSecurityProperties() {
        assertThat(securityProperties.jwtIssuer()).isEqualTo("portal-comunicacao");
        assertThat(securityProperties.jwtSecret()).isNotBlank();
        assertThat(securityProperties.jwtAccessTtlMinutes()).isEqualTo(15);
        assertThat(securityProperties.refreshTokenTtlHours()).isEqualTo(8);
        assertThat(securityProperties.refreshTokenRememberMeDays()).isEqualTo(30);
        assertThat(securityProperties.maxConcurrentSessions()).isEqualTo(3);
        assertThat(securityProperties.csrfEnabled()).isTrue();
        assertThat(securityProperties.corsAllowedOrigins()).containsExactly("http://localhost:4200");
    }
}
