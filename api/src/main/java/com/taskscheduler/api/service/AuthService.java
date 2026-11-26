package com.taskscheduler.api.service;

import com.taskscheduler.api.dto.response.AuthResponse;

public interface AuthService {
	AuthResponse register(String email, String password);
	AuthResponse login(String email, String password);
}
