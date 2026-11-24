package com.taskscheduller.api.integration.controller;

import com.taskscheduller.api.controller.AuthController;
import com.taskscheduller.api.controller.ExceptionController;
import com.taskscheduller.api.dto.response.AuthResponse;
import com.taskscheduller.api.integration.TestSecurityConfiguration;
import com.taskscheduller.api.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc
@Import({ExceptionController.class, TestSecurityConfiguration.class})
class AuthControllerTest {
    @Autowired MockMvc mockMvc;
    @MockitoBean AuthService authService;

    @Test
    void loginReturnsJwtFromService() throws Exception {
        when(authService.login("user@example.com", "secret")).thenReturn(new AuthResponse("jwt-token"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content("""
                                {"email":"user@example.com","password":"secret"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").value("jwt-token"));

        verify(authService).login("user@example.com", "secret");
    }

    @Test
    void loginRejectsInvalidEmailAndBlankPassword() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content("""
                                {"email":"invalid","password":" "}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.url").value("/api/auth/login"))
                .andExpect(jsonPath("$.details").isNotEmpty());

        verifyNoInteractions(authService);
    }
}
