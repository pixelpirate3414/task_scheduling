package com.taskscheduler.api.controller;

import com.taskscheduler.api.dto.request.AuthRequest;
import com.taskscheduler.api.dto.response.AuthResponse;
import com.taskscheduler.api.service.AuthService;
import com.taskscheduler.api.controller.docs.AuthControllerDocs;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerDocs {
	private final AuthService authService;

	@PostMapping("/login")
	public AuthResponse login(@Valid @RequestBody AuthRequest request) {
		return authService.login(request.email(), request.password());
	}
}
