package com.api.imagestorage.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.api.imagestorage.entity.User;
import com.api.imagestorage.exception.StorageException;
import com.api.imagestorage.exception.StorageFileNotFoundException;
import com.api.imagestorage.model.request.UserRequest;
import com.api.imagestorage.service.StorageService;

@Service
public class StorageServiceImpl implements StorageService {

	private static final String PRODUCT_IMAGE_PATH_STR = "uploads/images";
	private static final Path PRODUCT_IMAGE_PATH = Paths.get(PRODUCT_IMAGE_PATH_STR);

	@Override
	public String getStoredFilename(MultipartFile file, String fileName) {
		String ext = FilenameUtils.getExtension(file.getOriginalFilename());
		return "p" + fileName + "." + ext;
	}

	@Override
	public void init() {
		try {
			Files.createDirectories(PRODUCT_IMAGE_PATH);
		} catch (Exception e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}

	@Override
	public Path load(String fileName) {
		return PRODUCT_IMAGE_PATH.resolve(fileName);
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
			Path destinationFile = PRODUCT_IMAGE_PATH.resolve(Paths.get(storedFileName)).normalize().toAbsolutePath();
			if (!destinationFile.getParent().equals(PRODUCT_IMAGE_PATH.toAbsolutePath())) {
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
		Path destinationFile = PRODUCT_IMAGE_PATH.resolve(Paths.get(fileName)).normalize().toAbsolutePath();
		Files.delete(destinationFile);
	}

	@Override
	public void addAvatar(UserRequest userRequest, User user) {
		UUID uuid = UUID.randomUUID();
		String uuString = uuid.toString();
		user.setAvatar(getStoredFilename(userRequest.getAvatar(), uuString));
		store(userRequest.getAvatar(), user.getAvatar());
	}
	
	@Override
	public void editAvatar(UserRequest userRequest, User user) throws IOException {
		delete(user.getAvatar());
		UUID uuid = UUID.randomUUID();
		String uuString = uuid.toString();
		user.setAvatar(getStoredFilename(userRequest.getAvatar(), uuString));
		store(userRequest.getAvatar(), user.getAvatar());
	}


}
