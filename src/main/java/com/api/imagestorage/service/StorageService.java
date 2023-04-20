package com.api.imagestorage.service;

import java.io.IOException;
import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

	void init();
	
	public String getStoredFilename(MultipartFile file, String id);

	Path load(String fileName);

	Resource loadAsResource(String storedFileName);

	void store(MultipartFile file, String storedFileName);

	void delete(String fileName) throws IOException;
}
