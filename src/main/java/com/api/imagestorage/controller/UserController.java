package com.api.imagestorage.controller;

import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.imagestorage.model.request.UserRequest;
import com.api.imagestorage.model.response.UserResponse;
import com.api.imagestorage.service.StorageService;
import com.api.imagestorage.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	private StorageService storageService;

	@PostMapping
	public ResponseEntity<?> createNewUser(@ModelAttribute UserRequest userRequest) {
		UserResponse userResponse = userService.createNewUser(userRequest);
		return ResponseEntity.ok(userResponse);
	}

	@GetMapping("avatar/{fileName}")
	public ResponseEntity<?> getAvatar(@PathVariable String fileName) throws IOException {
		byte[] imageData = Files.readAllBytes(storageService.load(fileName));
		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(imageData);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getUser(@PathVariable Long id) {
		UserResponse userResponse = userService.getUserById(id);
		return ResponseEntity.ok(userResponse);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> editUser(@PathVariable Long id, @ModelAttribute UserRequest userRequest) throws IOException {
		UserResponse userResponse = userService.editUser(id, userRequest);
		return ResponseEntity.ok(userResponse);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Long id) throws IOException {
		UserResponse userResponse = userService.deleteUser(id);
		return ResponseEntity.ok(userResponse);
	}
}
