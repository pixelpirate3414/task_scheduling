package com.taskscheduller.api.service.impl;

import com.taskscheduller.api.dto.request.AuthRequest;
import com.taskscheduller.api.dto.response.AuthResponse;
import com.taskscheduller.api.entity.User;
import com.taskscheduller.api.repository.UserRepository;
import com.taskscheduller.api.security.JwtService;
import com.taskscheduller.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;

	@Override
	@Transactional
	public AuthResponse register(AuthRequest request) {
		if (userRepository.findByEmail(request.email()).isPresent()) {
			throw new IllegalArgumentException("Email is already registered");
		}

		userRepository.save(new User(request.email(), passwordEncoder.encode(request.password())));

		return login(request);
	}

	@Override
	public AuthResponse login(AuthRequest request) {
		var authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.email(), request.password())
		);
		return new AuthResponse(jwtService.generateToken(authentication));
	}
}
