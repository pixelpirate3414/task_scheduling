package com.taskscheduller.api.service;

import com.taskscheduller.api.dto.response.UserResponse;

public interface UserService {
	UserResponse getByEmail(String email);
}
