package com.mateus.apiteste;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.mateus.apiteste.services.S3Service;

@SpringBootApplication
public class ApitesteApplication implements CommandLineRunner{
	
	public static void main(String[] args) {
		SpringApplication.run(ApitesteApplication.class, args);		
	}

	
	//@Autowired
	//private S3Service s3Service;
	
	@Override
	public void run(String... args) throws Exception {		
		//s3Service.uploadFile("C:\\temp\\teste.sql");
	}	
	
}
