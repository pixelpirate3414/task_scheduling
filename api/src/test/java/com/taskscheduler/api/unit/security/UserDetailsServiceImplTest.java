package com.taskscheduler.api.unit.security;

import com.taskscheduler.api.entity.User;
import com.taskscheduler.api.repository.UserRepository;
import com.taskscheduler.api.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {
    @Mock UserRepository userRepository;

    @Test
    void loadsSpringUserDetailsFromStoredUser() {
        when(userRepository.findByEmail("user@example.com"))
                .thenReturn(Optional.of(new User("user@example.com", "encoded")));

        var details = new UserDetailsServiceImpl(userRepository).loadUserByUsername("user@example.com");

        assertThat(details.getUsername()).isEqualTo("user@example.com");
        assertThat(details.getPassword()).isEqualTo("encoded");
        assertThat(details.getAuthorities()).isEmpty();
    }

    @Test
    void throwsWhenUserDoesNotExist() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> new UserDetailsServiceImpl(userRepository).loadUserByUsername("missing@example.com"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found: missing@example.com");
    }
}
