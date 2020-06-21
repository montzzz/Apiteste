package com.mateus.apiteste.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

@Service
public class S3Service {

	private Logger LOG = org.slf4j.LoggerFactory.getLogger(S3Service.class);

	@Autowired
	private AmazonS3 s3client;

	@Value("${s3.bucket}")
	private String bucketName;

	public URI uploadFile(MultipartFile multiPartFile) {
		try {
			// pega o nome do arquivo original enviado pelo request
			String fileName = multiPartFile.getOriginalFilename();
			// cria a stream e busca ela do tipo "MultiPartFile"
			InputStream is = multiPartFile.getInputStream();
			// Busca o content-type do arquivo recebido
			String contentType = multiPartFile.getContentType();

			return uploadFile(is, fileName, contentType);
		} catch (IOException e) {
			throw new RuntimeException("Erro de IO: " + e.getMessage());
		}
	}

	public URI uploadFile(InputStream is, String fileName, String contentType) {

		try {
			// cria a var do tipo Metadata
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentType(contentType);
			// faz o log antes de tentar enviar o dado para o bucket do s3
			LOG.info("Iniciando o Upload");
			// tenta fazer o put do file para o s3
			s3client.putObject(bucketName, fileName, is, meta);
			// caso ocorra com sucesso, loga na aplicação
			LOG.info("Upload finalizado");

			return s3client.getUrl(bucketName, fileName).toURI();
		} catch (URISyntaxException e) {
			// Caso aconteça erro
			throw new RuntimeException("Erro ao converter a URL para URI.");
		}
	}

}
