package com.taskscheduller.api.service;

import com.taskscheduller.api.dto.request.AuthRequest;
import com.taskscheduller.api.dto.response.AuthResponse;

public interface AuthService {
	AuthResponse register(AuthRequest request);
	AuthResponse login(AuthRequest request);
}
