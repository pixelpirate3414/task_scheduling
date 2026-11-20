package com.taskscheduller.api.controller;

import com.taskscheduller.api.dto.request.AuthRequest;
import com.taskscheduller.api.dto.response.AuthResponse;
import com.taskscheduller.api.dto.response.UserResponse;
import com.taskscheduller.api.mapper.UserMapper;
import com.taskscheduller.api.repository.UserRepository;
import com.taskscheduller.api.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {
	private final AuthService authService;
	private final UserRepository userRepository;
	private final UserMapper userMapper;

	@PostMapping
	public AuthResponse register(@Valid @RequestBody AuthRequest request) {
		return authService.register(request);
	}

	@GetMapping
	public UserResponse getCurrentUser(Authentication authentication) {
		var user = userRepository.findByEmail(authentication.getName())
				.orElseThrow(() -> new IllegalStateException("Authenticated user was not found"));
		return userMapper.toResponse(user);
	}
}
