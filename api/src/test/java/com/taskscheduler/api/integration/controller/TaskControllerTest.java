package com.taskscheduler.api.integration.controller;

import com.taskscheduler.api.controller.ExceptionController;
import com.taskscheduler.api.controller.TaskController;
import com.taskscheduler.api.dto.response.TaskResponse;
import com.taskscheduler.api.entity.TaskStatus;
import com.taskscheduler.api.exception.TaskNotFoundException;
import com.taskscheduler.api.integration.TestSecurityConfiguration;
import com.taskscheduler.api.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc
@Import({ExceptionController.class, TestSecurityConfiguration.class})
class TaskControllerTest {
    @Autowired MockMvc mockMvc;
    @MockitoBean TaskService taskService;

    @Test
    void createTaskValidatesRequestAndReturnsTask() throws Exception {
        when(taskService.createTask("user@example.com", "title", "body"))
                .thenReturn(new TaskResponse("title", "body", TaskStatus.IN_PROGRESS));

        mockMvc.perform(post("/api/tasks")
                        .with(jwt().jwt(jwt -> jwt.subject("user@example.com")))
                        .contentType("application/json")
                        .content("""
                                {"title":"title","body":"body"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.body").value("body"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));

        verify(taskService).createTask("user@example.com", "title", "body");
    }

    @Test
    void createTaskRejectsBlankTitle() throws Exception {
        mockMvc.perform(post("/api/tasks")
                        .with(jwt().jwt(jwt -> jwt.subject("user@example.com")))
                        .contentType("application/json")
                        .content("""
                                {"title":" ","body":"body"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.url").value("/api/tasks"))
                .andExpect(jsonPath("$.details").value(org.hamcrest.Matchers.containsString("title")));

        verifyNoInteractions(taskService);
    }

    @Test
    void getTaskReturnsServiceResponseAndPassesAuthenticatedOwner() throws Exception {
        when(taskService.getTask("user@example.com", 42L))
                .thenReturn(new TaskResponse("title", null, TaskStatus.DONE));

        mockMvc.perform(get("/api/tasks/42").with(jwt().jwt(jwt -> jwt.subject("user@example.com"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.status").value("DONE"));

        verify(taskService).getTask("user@example.com", 42L);
    }

    @Test
    void serviceNotFoundExceptionBecomesNotFoundResponse() throws Exception {
        when(taskService.getTask("user@example.com", 42L)).thenThrow(new TaskNotFoundException(42L));

        mockMvc.perform(get("/api/tasks/42").with(jwt().jwt(jwt -> jwt.subject("user@example.com"))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Not Found"))
                .andExpect(jsonPath("$.details").value("Task not found: 42"))
                .andExpect(jsonPath("$.url").value("/api/tasks/42"));
    }

    @Test
    void deleteTaskReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/tasks/42").with(jwt().jwt(jwt -> jwt.subject("user@example.com"))))
                .andExpect(status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content().string(""));

        verify(taskService).deleteTask("user@example.com", 42L);
    }

    @Test
    void completeAndReopenReturnUpdatedTask() throws Exception {
        when(taskService.complete("user@example.com", 42L))
                .thenReturn(new TaskResponse("title", null, TaskStatus.DONE));
        when(taskService.reopen("user@example.com", 42L))
                .thenReturn(new TaskResponse("title", null, TaskStatus.IN_PROGRESS));

        mockMvc.perform(patch("/api/tasks/42/complete").with(jwt().jwt(jwt -> jwt.subject("user@example.com"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DONE"));
        mockMvc.perform(patch("/api/tasks/42/reopen").with(jwt().jwt(jwt -> jwt.subject("user@example.com"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));

        verify(taskService).complete("user@example.com", 42L);
        verify(taskService).reopen("user@example.com", 42L);
    }
}
