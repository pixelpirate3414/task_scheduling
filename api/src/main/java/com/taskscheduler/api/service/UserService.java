package com.taskscheduler.api.service;

import com.taskscheduler.api.dto.response.UserResponse;

public interface UserService {
	UserResponse getByEmail(String email);
}
