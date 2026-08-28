package br.com.unimedceara.portalcomunicacao.documento.infrastructure.storage;

import br.com.unimedceara.portalcomunicacao.configuration.properties.StorageProperties;
import br.com.unimedceara.portalcomunicacao.documento.application.port.ObjectStorageClient;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.InputStream;

/**
 * Implementação de {@link ObjectStorageClient} via AWS SDK v2 (compatível com MinIO, DEC-013).
 */
@Component
public class S3ObjectStorageClient implements ObjectStorageClient {

    private final S3Client s3Client;
    private final StorageProperties storageProperties;

    public S3ObjectStorageClient(S3Client s3Client, StorageProperties storageProperties) {
        this.s3Client = s3Client;
        this.storageProperties = storageProperties;
    }

    @Override
    public InputStream download(String referenciaObjeto) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(storageProperties.bucket())
                .key(referenciaObjeto)
                .build();
        ResponseInputStream<GetObjectResponse> response = s3Client.getObject(request);
        return response;
    }
}
