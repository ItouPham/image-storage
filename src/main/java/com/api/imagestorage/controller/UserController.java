package com.api.imagestorage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.imagestorage.model.request.UserRequest;
import com.api.imagestorage.model.response.UserResponse;
import com.api.imagestorage.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	
	@PostMapping
	public ResponseEntity<?> createNewUser(@RequestBody UserRequest userRequest){
		UserResponse userResponse = userService.createNewUser(userRequest);
		return ResponseEntity.ok(userResponse);
	}
}
