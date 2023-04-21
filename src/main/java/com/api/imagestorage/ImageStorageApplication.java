package com.api.imagestorage;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.api.imagestorage.service.StorageService;

@SpringBootApplication
public class ImageStorageApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImageStorageApplication.class, args);
	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args -> {
			storageService.init();
		});
	}
}
