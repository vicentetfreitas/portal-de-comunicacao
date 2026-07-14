package br.com.unimedceara.portalcomunicacao.infrastructure.security.filter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:pf-sec-test.properties")
class JwtStructureValidatorTest {

    @Autowired
    private JwtStructureValidator jwtStructureValidator;

    @Test
    void shouldAcceptValidJwtStructure() {
        String token = buildToken("{\"sub\":\"user-1\"}");

        assertThat(jwtStructureValidator.hasValidStructure(token)).isTrue();
        assertThat(jwtStructureValidator.extractSubject(token)).contains("user-1");
    }

    @Test
    void shouldRejectInvalidJwtStructure() {
        assertThat(jwtStructureValidator.hasValidStructure("invalid.token")).isFalse();
        assertThat(jwtStructureValidator.extractSubject("invalid.token")).isEmpty();
    }

    private String buildToken(String payloadJson) {
        String header = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString("{\"alg\":\"none\",\"typ\":\"JWT\"}".getBytes(StandardCharsets.UTF_8));
        String payload = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(payloadJson.getBytes(StandardCharsets.UTF_8));
        String signature = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString("signature".getBytes(StandardCharsets.UTF_8));
        return header + "." + payload + "." + signature;
    }
}
