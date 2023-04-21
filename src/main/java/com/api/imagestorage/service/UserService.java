package com.api.imagestorage.service;

import java.io.IOException;

import com.api.imagestorage.model.request.UserRequest;
import com.api.imagestorage.model.response.UserResponse;

public interface UserService {

	UserResponse createNewUser(UserRequest userRequest);

	UserResponse getUserById(Long id);

	UserResponse editUser(Long id, UserRequest userRequest) throws IOException;

	UserResponse deleteUser(Long id) throws IOException;

}
