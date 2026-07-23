package br.com.unimedceara.portalcomunicacao.configuration.properties;

import br.com.unimedceara.portalcomunicacao.support.annotation.PlatformFoundationSliceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@PlatformFoundationSliceTest
class SecurityPropertiesTest {

    @Autowired
    private SecurityProperties securityProperties;

    @Test
    void shouldLoadSecurityProperties() {
        assertThat(securityProperties.jwtSecret()).isEqualTo("test-jwt-secret-32-characters-minimum");
        assertThat(securityProperties.csrfEnabled()).isTrue();
        assertThat(securityProperties.corsAllowedOrigins()).contains("http://localhost:4200");
    }
}
