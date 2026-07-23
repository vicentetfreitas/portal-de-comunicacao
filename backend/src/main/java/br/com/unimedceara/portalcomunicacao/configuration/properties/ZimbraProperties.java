package br.com.unimedceara.portalcomunicacao.configuration.properties;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Propriedades de integração Zimbra (IMAP / SMTP / SOAP) — FT-AUTH.
 */
@Validated
@ConfigurationProperties(prefix = "application.zimbra")
public record ZimbraProperties(
        @NotBlank String loginPageUrl,
        @NotBlank String imapHost,
        @Min(1) int imapPort,
        boolean imapSsl,
        @NotBlank String smtpHost,
        @Min(1) int smtpPort,
        boolean smtpSsl,
        boolean smtpStartTls,
        @NotBlank String soapUrl,
        @Min(1) int timeoutMs) {
}
