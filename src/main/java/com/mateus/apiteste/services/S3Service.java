package com.mateus.apiteste.services;

import java.io.File;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class S3Service {
	
	private Logger LOG = org.slf4j.LoggerFactory.getLogger(S3Service.class);
	
	@Autowired
	private AmazonS3 s3client;
	
	@Value("${s3.bucket}")
	private String bucketName;
	
	public void uploadFile(String localFilePath) {
		
		try {
			// Carrega o arquivo em memoria
			File file = new File(localFilePath);
			// faz o log antes de tentar enviar o dado para o bucket do s3
			LOG.info("Iniciando o Upload");			
			//tenta fazer o put do file para o s3
			s3client.putObject(new PutObjectRequest(bucketName, "teste", file));
			// caso ocorra com sucesso, loga na aplicação
			LOG.info("Upload finalizado");
		} catch (AmazonServiceException e) {
			LOG.info("AmazonServiceException" + e.getMessage());
			LOG.info("Status code: " + e.getErrorCode());
		} catch (AmazonClientException e) {
			LOG.info("AmazonClientException" + e.getMessage());
		}			
	}

}
