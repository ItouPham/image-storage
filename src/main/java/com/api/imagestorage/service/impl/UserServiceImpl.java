package com.api.imagestorage.service.impl;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
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
		if(!userRequest.getImage().isEmpty()) {
			UUID uuid = UUID.randomUUID();
            String uuString = uuid.toString();
            user.setImage(storageService.getStoredFilename(userRequest.getImage(), uuString));
            storageService.store(userRequest.getImage(), user.getImage());
		}
		userRepository.save(user);
		BeanUtils.copyProperties(user, userResponse);
		return userResponse;
	}

}
