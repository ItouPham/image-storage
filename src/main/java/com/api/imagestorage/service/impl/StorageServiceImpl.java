package com.api.imagestorage.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.api.imagestorage.config.StorageProperties;
import com.api.imagestorage.exception.StorageException;
import com.api.imagestorage.exception.StorageFileNotFoundException;
import com.api.imagestorage.service.StorageService;

@Service
public class StorageServiceImpl implements StorageService {
	private final Path rootLocation;
	
	@Override
	public String getStoredFilename(MultipartFile file, String id) {
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());
        return "p" + id + "." + ext;
    }

	public StorageServiceImpl(StorageProperties properties) {
		this.rootLocation = Paths.get(properties.getLocation());
	}

	@Override
	public void init() {
		try {
            Files.createDirectories(rootLocation);
        } catch (Exception e) {
            throw new StorageException("Could not initialize storage", e);
        }
	}

	@Override
	public Path load(String fileName) {
		return rootLocation.resolve(fileName);
	}

	@Override
	public Resource loadAsResource(String fileName) {
		try {
            Path file = load(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            throw new StorageFileNotFoundException("Could not read file: " + fileName);
        } catch (Exception e) {
            throw new StorageFileNotFoundException("Could not read file: " + fileName);
        }
	}

	@Override
	public void store(MultipartFile file, String storedFileName) {
		try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file");
            }
            Path destinationFile = this.rootLocation.resolve(Paths.get(storedFileName)).normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new StorageException("Cannot store file outside current directory");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            throw new StorageException("Failed to store file", e);
        }
	}

	@Override
	public void delete(String fileName) throws IOException {
		Path destinationFile = rootLocation.resolve(Paths.get(fileName)).normalize().toAbsolutePath();
        Files.delete(destinationFile);
	}

	

}
