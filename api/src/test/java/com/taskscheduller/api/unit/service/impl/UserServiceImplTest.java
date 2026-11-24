package com.taskscheduller.api.unit.service.impl;

import com.taskscheduller.api.entity.User;
import com.taskscheduller.api.exception.UserNotFoundException;
import com.taskscheduller.api.mapper.UserMapper;
import com.taskscheduller.api.repository.UserRepository;
import com.taskscheduller.api.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock UserRepository userRepository;
    @Mock UserMapper userMapper;
    @InjectMocks UserServiceImpl userService;

    @Test
    void getByEmailReturnsMappedUser() {
        var user = new User("user@example.com", "encoded");
        var response = new com.taskscheduller.api.dto.response.UserResponse("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(response);

        assertThat(userService.getByEmail("user@example.com")).isSameAs(response);
        verify(userMapper).toResponse(user);
    }

    @Test
    void getByEmailThrowsWhenUserDoesNotExist() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getByEmail("missing@example.com"))
                .isInstanceOf(UserNotFoundException.class);
    }
}
