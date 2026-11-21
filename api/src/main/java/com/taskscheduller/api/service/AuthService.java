package com.taskscheduller.api.service;

import com.taskscheduller.api.dto.request.AuthRequest;
import com.taskscheduller.api.dto.response.AuthResponse;

public interface AuthService {
	AuthResponse register(String email, String password);
	AuthResponse login(String email, String password);
}
