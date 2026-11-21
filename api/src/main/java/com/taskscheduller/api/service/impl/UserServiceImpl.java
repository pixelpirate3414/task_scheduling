package com.taskscheduller.api.service.impl;

import com.taskscheduller.api.dto.response.UserResponse;
import com.taskscheduller.api.exception.UserNotFoundException;
import com.taskscheduller.api.mapper.UserMapper;
import com.taskscheduller.api.repository.UserRepository;
import com.taskscheduller.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final UserMapper userMapper;

	@Override
	public UserResponse getByEmail(String email) {
		var user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UserNotFoundException(email));

		return userMapper.toResponse(user);
	}
}
