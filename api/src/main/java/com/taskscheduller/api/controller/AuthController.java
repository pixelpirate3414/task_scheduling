package com.taskscheduller.api.controller;

import com.taskscheduller.api.dto.request.AuthRequest;
import com.taskscheduller.api.dto.response.AuthResponse;
import com.taskscheduller.api.service.AuthService;
import com.taskscheduller.api.controller.docs.AuthControllerDocs;
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
