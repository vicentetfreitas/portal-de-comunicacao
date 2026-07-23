package br.com.unimedceara.portalcomunicacao.configuration.locale;

import br.com.unimedceara.portalcomunicacao.support.annotation.PlatformFoundationSliceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@PlatformFoundationSliceTest
class LocaleConfigurationTest {

    @Autowired
    private Locale applicationLocale;

    @Test
    void shouldConfigureDefaultLocaleFromProperties() {
        assertThat(applicationLocale).isEqualTo(Locale.forLanguageTag("pt-BR"));
        assertThat(Locale.getDefault()).isEqualTo(Locale.forLanguageTag("pt-BR"));
    }
}
