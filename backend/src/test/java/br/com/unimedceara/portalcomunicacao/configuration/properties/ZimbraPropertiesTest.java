package br.com.unimedceara.portalcomunicacao.configuration.properties;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:pf-conf-test.properties", properties = {
        "application.zimbra.auth-url=https://zimbra.example.com/auth",
        "application.zimbra.validate-url=https://zimbra.example.com/validate",
        "application.zimbra.timeout-ms=8000"
})
class ZimbraPropertiesTest {

    @Autowired
    private ZimbraProperties zimbraProperties;

    @Test
    void shouldLoadZimbraPropertiesFromEnvironmentPlaceholders() {
        assertThat(zimbraProperties.authUrl()).isEqualTo("https://zimbra.example.com/auth");
        assertThat(zimbraProperties.validateUrl()).isEqualTo("https://zimbra.example.com/validate");
        assertThat(zimbraProperties.timeoutMs()).isEqualTo(8000);
    }
}
