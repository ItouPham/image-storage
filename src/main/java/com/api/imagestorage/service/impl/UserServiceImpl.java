package com.api.imagestorage.service.impl;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.imagestorage.entity.User;
import com.api.imagestorage.model.request.UserRequest;
import com.api.imagestorage.model.response.UserResponse;
import com.api.imagestorage.repository.UserRepository;
import com.api.imagestorage.service.StorageService;
import com.api.imagestorage.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private StorageService storageService;

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserResponse createNewUser(UserRequest userRequest) {
		User user = new User();
		UserResponse userResponse = new UserResponse();
		BeanUtils.copyProperties(userRequest, user);
		if (!userRequest.getAvatar().isEmpty()) {
			storageService.addAvatar(userRequest, user);
		}
		userRepository.save(user);
		BeanUtils.copyProperties(user, userResponse);
		return userResponse;
	}

	@Override
	public UserResponse getUserById(Long id) {
		UserResponse userResponse = new UserResponse();
		User user = userRepository.findById(id).orElse(null);
		if (user != null) {
			BeanUtils.copyProperties(user, userResponse);
		}
		return userResponse;
	}

	@Override
	public UserResponse editUser(Long id, UserRequest userRequest) throws IOException {
		UserResponse userResponse = new UserResponse();
		User user = userRepository.findById(id).orElse(null);
		if (user != null) {
			BeanUtils.copyProperties(userRequest, user);
			if (!userRequest.getAvatar().isEmpty()) {
				storageService.editAvatar(userRequest, user);
			}
			userRepository.save(user);
			BeanUtils.copyProperties(user, userResponse);
		} else {
			throw new RuntimeException("User Not Found");
		}
		return userResponse;
	}

	@Override
	public UserResponse deleteUser(Long id) throws IOException {
		UserResponse userResponse = new UserResponse();
		User user = userRepository.findById(id).orElse(null);
		if (user != null) {
			BeanUtils.copyProperties(user, userResponse);
			storageService.delete(user.getAvatar());
			userRepository.delete(user);
		} else {
			throw new RuntimeException("User Not Found");
		}
		return userResponse;
	}

}
