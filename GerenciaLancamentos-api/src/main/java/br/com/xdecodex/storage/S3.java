package br.com.xdecodex.storage;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import br.com.xdecodex.config.property.GerenciaApiProperty;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectTaggingRequest;
import software.amazon.awssdk.services.s3.model.Tagging;

@Component
public class S3 {

    private static final Logger logger = LoggerFactory.getLogger(S3.class);

    @Autowired
    private GerenciaApiProperty property;

    @Autowired
    private S3Client s3Client;
    

    public String salvarTemporariamente(MultipartFile arquivo) {
        String nomeUnico = gerarNomeUnico(arquivo.getOriginalFilename());

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(property.getS3().getBucket())
                    .key(nomeUnico)
                    .contentType(arquivo.getContentType())  
                    .contentLength(arquivo.getSize())       
                    .build(); // Removida a ACL

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(arquivo.getInputStream(), arquivo.getSize()));

            if (logger.isDebugEnabled()) {
                logger.debug("Arquivo {} enviado com sucesso para o S3.", arquivo.getOriginalFilename());
            }

            return nomeUnico;
        } catch (IOException e) {
            throw new RuntimeException("Problemas ao tentar enviar o arquivo para o S3.", e);
        }
    }
    
    public void remove(String objeto) {
        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
            .bucket(property.getS3().getBucket())
            .key(objeto)
            .build();

        s3Client.deleteObject(deleteRequest);
    }

	public void toReplace(String objetoAntigo, String objetoNovo) {
		if (StringUtils.hasText(objetoAntigo)) {
			this.remove(objetoAntigo);
		}

		create(objetoNovo);
	}	

    public String configurarUrl(String objeto) {
        return "https://" + property.getS3().getBucket() + ".s3.amazonaws.com/" + objeto;
    }
    
    public void create(String objeto) {
    	PutObjectTaggingRequest putObjectTaggingRequest = PutObjectTaggingRequest.builder()
                .bucket(property.getS3().getBucket())
                .key(objeto)
                .tagging(Tagging.builder().tagSet(Collections.emptyList()).build())
                .build();

        s3Client.putObjectTagging(putObjectTaggingRequest);
	}

    private String gerarNomeUnico(String originalFilename) {
        return UUID.randomUUID().toString() + "_" + originalFilename;
    }

}
