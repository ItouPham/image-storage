package com.api.imagestorage.service;

import com.api.imagestorage.model.request.UserRequest;
import com.api.imagestorage.model.response.UserResponse;

public interface UserService {

	UserResponse createNewUser(UserRequest userRequest);

}
