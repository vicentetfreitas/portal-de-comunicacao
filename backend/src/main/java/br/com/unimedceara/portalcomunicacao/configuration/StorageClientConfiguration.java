package br.com.unimedceara.portalcomunicacao.configuration;

import br.com.unimedceara.portalcomunicacao.configuration.properties.StorageProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;

/**
 * Cliente do Object Storage S3-compatible (DEC-013). AWS SDK v2 é compatível com MinIO —
 * não requer SDK proprietário do provedor (ver structural-simplification-plan-w2.md).
 */
@Configuration
public class StorageClientConfiguration {

    @Bean
    public S3Client s3Client(StorageProperties storageProperties) {
        return S3Client.builder()
                .endpointOverride(URI.create(storageProperties.endpoint()))
                .region(Region.of(storageProperties.region()))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(storageProperties.accessKey(), storageProperties.secretKey())))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(storageProperties.pathStyleAccess())
                        .build())
                .build();
    }
}
