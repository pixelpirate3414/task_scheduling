package com.taskscheduller.api.integration.controller;

import com.taskscheduller.api.controller.ExceptionController;
import com.taskscheduller.api.controller.UserController;
import com.taskscheduller.api.dto.response.AuthResponse;
import com.taskscheduller.api.dto.response.UserResponse;
import com.taskscheduller.api.integration.TestSecurityConfiguration;
import com.taskscheduller.api.service.AuthService;
import com.taskscheduller.api.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@Import({ExceptionController.class, TestSecurityConfiguration.class})
@TestPropertySource(properties = {
        "logging.level.org.springframework.security=TRACE"
})
class UserControllerTest {
    @Autowired MockMvc mockMvc;
    @MockitoBean AuthService authService;
    @MockitoBean UserService userService;

    @Test
    void registerReturnsJwtFromService() throws Exception {
        when(authService.register("user@example.com", "secret")).thenReturn(new AuthResponse("registered-jwt"));

        mockMvc.perform(post("/api/user")
                        .contentType("application/json")
                        .content("""
                                {"email":"user@example.com","password":"secret"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").value("registered-jwt"));

        verify(authService).register("user@example.com", "secret");
    }

    @Test
    void currentUserUsesAuthenticatedEmailAndReturnsUser() throws Exception {
        when(userService.getByEmail("user@example.com")).thenReturn(new UserResponse("user@example.com"));

        mockMvc.perform(get("/api/user")
                .with(jwt().jwt(jwt -> jwt.subject("user@example.com"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@example.com"));

        verify(userService).getByEmail("user@example.com");
    }

    @Test
    void registerRejectsMissingEmailAndPassword() throws Exception {
        mockMvc.perform(post("/api/user")
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.url").value("/api/user"))
                .andExpect(jsonPath("$.details").isNotEmpty());

        verifyNoInteractions(authService);
    }
}
