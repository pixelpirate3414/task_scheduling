package com.taskscheduler.api.unit.service.impl;

import com.taskscheduler.api.entity.User;
import com.taskscheduler.api.exception.UserAlreadyRegisteredException;
import com.taskscheduler.api.repository.UserRepository;
import com.taskscheduler.api.security.JwtService;
import com.taskscheduler.api.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock UserRepository userRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock AuthenticationManager authenticationManager;
    @Mock JwtService jwtService;
    @InjectMocks AuthServiceImpl authService;

    @Test
    void registerPersistsEncodedPasswordAuthenticatesAndReturnsToken() {
        var authentication = mock(Authentication.class);
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("secret")).thenReturn("encoded-secret");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtService.generateToken(authentication)).thenReturn("jwt-value");

        assertThat(authService.register("user@example.com", "secret").jwt()).isEqualTo("jwt-value");
        verify(userRepository).save(argThat(user -> user.getEmail().equals("user@example.com") && user.getPassword().equals("encoded-secret")));
        verify(authenticationManager).authenticate(argThat(token -> token.getPrincipal().equals("user@example.com") && token.getCredentials().equals("secret")));
    }

    @Test
    void registerRejectsExistingEmailWithoutCreatingUser() {
        when(userRepository.findByEmail("user@example.com"))
                .thenReturn(Optional.of(new User("user@example.com", "encoded")));

        assertThatThrownBy(() -> authService.register("user@example.com", "secret"))
                .isInstanceOf(UserAlreadyRegisteredException.class);
        verifyNoInteractions(passwordEncoder, authenticationManager, jwtService);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void loginAuthenticatesCredentialsAndReturnsToken() {
        var authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtService.generateToken(authentication)).thenReturn("jwt-value");

        assertThat(authService.login("user@example.com", "secret").jwt()).isEqualTo("jwt-value");
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(authentication);
        verifyNoInteractions(userRepository, passwordEncoder);
    }
}
