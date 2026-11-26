package com.taskscheduler.api.service.impl;

import com.taskscheduler.api.dto.response.UserResponse;
import com.taskscheduler.api.exception.UserNotFoundException;
import com.taskscheduler.api.mapper.UserMapper;
import com.taskscheduler.api.repository.UserRepository;
import com.taskscheduler.api.service.UserService;
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
