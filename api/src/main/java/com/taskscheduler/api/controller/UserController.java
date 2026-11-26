package com.taskscheduler.api.controller;

import com.taskscheduler.api.dto.request.AuthRequest;
import com.taskscheduler.api.dto.response.AuthResponse;
import com.taskscheduler.api.dto.response.UserResponse;
import com.taskscheduler.api.service.AuthService;
import com.taskscheduler.api.service.UserService;
import com.taskscheduler.api.controller.docs.UserControllerDocs;
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
public class UserController implements UserControllerDocs {
	private final AuthService authService;
	private final UserService userService;

	@PostMapping
	public AuthResponse register(@Valid @RequestBody AuthRequest request) {
		return authService.register(request.email(), request.password());
	}

	@GetMapping
	public UserResponse getCurrentUser(Authentication authentication) {
		return userService.getByEmail(authentication.getName());
	}
}
