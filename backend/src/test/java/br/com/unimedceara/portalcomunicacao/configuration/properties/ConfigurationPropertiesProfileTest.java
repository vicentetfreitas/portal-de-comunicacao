package br.com.unimedceara.portalcomunicacao.configuration.properties;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles({"test-slice", "local"})
@TestPropertySource(locations = "classpath:pf-conf-profile-test.properties")
class ConfigurationPropertiesLocalProfileTest {

    @Autowired
    private SecurityProperties securityProperties;

    @Value("${spring.jpa.show-sql}")
    private boolean showSql;

    @Test
    void shouldLoadPropertiesForLocalProfile() {
        assertThat(securityProperties.corsAllowedOrigins())
                .containsExactly("http://localhost:4200", "http://localhost:8080");
        assertThat(showSql).isTrue();
    }
}

@SpringBootTest
@ActiveProfiles({"test-slice", "dev"})
@TestPropertySource(locations = "classpath:pf-conf-profile-test.properties")
class ConfigurationPropertiesDevProfileTest {

    @Autowired
    private SecurityProperties securityProperties;

    @Test
    void shouldLoadPropertiesForDevProfile() {
        assertThat(securityProperties.corsAllowedOrigins())
                .containsExactly("https://dev.portal.unimedceara.com.br");
    }
}

@SpringBootTest
@ActiveProfiles({"test-slice", "hml"})
@TestPropertySource(locations = "classpath:pf-conf-profile-test.properties")
class ConfigurationPropertiesHmlProfileTest {

    @Autowired
    private PersistenceProperties persistenceProperties;

    @Value("${spring.jpa.show-sql}")
    private boolean showSql;

    @Test
    void shouldLoadPropertiesForHmlProfile() {
        assertThat(persistenceProperties.poolMaxSize()).isEqualTo(20);
        assertThat(showSql).isFalse();
    }
}
