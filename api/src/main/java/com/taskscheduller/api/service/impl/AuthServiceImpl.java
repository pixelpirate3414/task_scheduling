package com.taskscheduller.api.service.impl;

import com.taskscheduller.api.dto.response.AuthResponse;
import com.taskscheduller.api.entity.User;
import com.taskscheduller.api.exception.UserAlreadyRegisteredException;
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
	public AuthResponse register(String email, String password) {
		if (userRepository.findByEmail(email).isPresent()) {
			throw new UserAlreadyRegisteredException(email);
		}

		userRepository.save(new User(email, passwordEncoder.encode(password)));

		var authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(email, password)
		);

		return new AuthResponse(jwtService.generateToken(authentication));
	}

	@Override
	public AuthResponse login(String email, String password) {
		var authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(email, password)
		);

		return new AuthResponse(jwtService.generateToken(authentication));
	}
}
