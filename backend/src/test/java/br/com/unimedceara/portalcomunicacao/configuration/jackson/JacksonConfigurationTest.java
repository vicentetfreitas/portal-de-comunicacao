package br.com.unimedceara.portalcomunicacao.configuration.jackson;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.context.annotation.Import;
import tools.jackson.databind.json.JsonMapper;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@Import(JacksonConfiguration.class)
class JacksonConfigurationTest {

    @Autowired
    private JsonMapper jsonMapper;

    @Test
    void shouldSerializeInstantAsIsoString() throws Exception {
        String json = jsonMapper.writeValueAsString(Instant.parse("2026-07-08T14:00:00Z"));

        assertThat(json).isEqualTo("\"2026-07-08T14:00:00Z\"");
    }

    @Test
    void shouldDeserializeInstantFromIsoString() throws Exception {
        Instant result = jsonMapper.readValue("\"2026-07-08T14:00:00Z\"", Instant.class);

        assertThat(result).isEqualTo(Instant.parse("2026-07-08T14:00:00Z"));
    }
}
