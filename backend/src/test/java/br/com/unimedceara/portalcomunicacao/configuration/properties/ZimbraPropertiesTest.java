package br.com.unimedceara.portalcomunicacao.configuration.properties;

import br.com.unimedceara.portalcomunicacao.support.annotation.PlatformFoundationSliceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@PlatformFoundationSliceTest
@TestPropertySource(
        properties = {
            "application.zimbra.login-page-url=http://localhost:9000/auth",
            "application.zimbra.imap-host=imap.example.com",
            "application.zimbra.imap-port=993",
            "application.zimbra.imap-ssl=true",
            "application.zimbra.smtp-host=smtp.example.com",
            "application.zimbra.smtp-port=587",
            "application.zimbra.smtp-ssl=false",
            "application.zimbra.smtp-starttls=true",
            "application.zimbra.soap-url=https://mail.example.com/service/soap",
            "application.zimbra.timeout-ms=8000"
        })
class ZimbraPropertiesTest {

    @Autowired
    private ZimbraProperties zimbraProperties;

    @Test
    void shouldLoadZimbraPropertiesFromEnvironmentPlaceholders() {
        assertThat(zimbraProperties.loginPageUrl()).isEqualTo("http://localhost:9000/auth");
        assertThat(zimbraProperties.imapHost()).isEqualTo("imap.example.com");
        assertThat(zimbraProperties.soapUrl()).isEqualTo("https://mail.example.com/service/soap");
        assertThat(zimbraProperties.timeoutMs()).isEqualTo(8000);
    }
}
