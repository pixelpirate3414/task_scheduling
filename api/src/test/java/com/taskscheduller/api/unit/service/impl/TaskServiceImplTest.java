package com.taskscheduller.api.unit.service.impl;

import com.taskscheduller.api.dto.response.TaskResponse;
import com.taskscheduller.api.entity.Task;
import com.taskscheduller.api.entity.TaskStatus;
import com.taskscheduller.api.entity.User;
import com.taskscheduller.api.exception.TaskNotFoundException;
import com.taskscheduller.api.exception.UserNotFoundException;
import com.taskscheduller.api.mapper.TaskMapper;
import com.taskscheduller.api.repository.TaskRepository;
import com.taskscheduller.api.repository.UserRepository;
import com.taskscheduller.api.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {
    @Mock TaskRepository taskRepository;
    @Mock UserRepository userRepository;
    @Mock TaskMapper taskMapper;
    @InjectMocks TaskServiceImpl taskService;

    @Test
    void createTaskSavesTaskForOwnerAndReturnsMappedTask() {
        var owner = new User("user@example.com", "encoded");
        var task = new Task("title", "body", owner);
        var response = new TaskResponse("title", "body", TaskStatus.IN_PROGRESS);
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(owner));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(taskMapper.toResponse(task)).thenReturn(response);

        assertThat(taskService.createTask("user@example.com", "title", "body")).isSameAs(response);
        verify(taskRepository).save(argThat(saved -> saved.getOwner() == owner && saved.getTitle().equals("title") && saved.getBody().equals("body")));
    }

    @Test
    void createTaskFailsWhenOwnerDoesNotExist() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.createTask("missing@example.com", "title", "body"))
                .isInstanceOf(UserNotFoundException.class);
        verifyNoInteractions(taskRepository, taskMapper);
    }

    @Test
    void getTaskReturnsMappedOwnedTask() {
        var task = task("user@example.com");
        var response = new TaskResponse("title", "body", TaskStatus.IN_PROGRESS);
        when(taskRepository.findByIdAndOwnerEmail(7L, "user@example.com")).thenReturn(Optional.of(task));
        when(taskMapper.toResponse(task)).thenReturn(response);

        assertThat(taskService.getTask("user@example.com", 7L)).isSameAs(response);
    }

    @Test
    void deleteTaskDeletesOwnedTask() {
        var task = task("user@example.com");
        when(taskRepository.findByIdAndOwnerEmail(7L, "user@example.com")).thenReturn(Optional.of(task));

        taskService.deleteTask("user@example.com", 7L);

        verify(taskRepository).delete(task);
    }

    @Test
    void completeChangesTaskStateAndReturnsMappedTask() {
        var task = task("user@example.com");
        var response = mock(TaskResponse.class);
        when(taskRepository.findByIdAndOwnerEmail(7L, "user@example.com")).thenReturn(Optional.of(task));
        when(taskMapper.toResponse(task)).thenReturn(response);

        assertThat(taskService.complete("user@example.com", 7L)).isSameAs(response);
        assertThat(task.getStatus()).isEqualTo(TaskStatus.DONE);
    }

    @Test
    void reopenChangesCompletedTaskToInProgress() {
        var task = task("user@example.com");
        task.complete();
        when(taskRepository.findByIdAndOwnerEmail(7L, "user@example.com")).thenReturn(Optional.of(task));
        when(taskMapper.toResponse(task)).thenReturn(new TaskResponse("title", "body", TaskStatus.IN_PROGRESS));

        taskService.reopen("user@example.com", 7L);

        assertThat(task.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
        assertThat(task.getDoneAt()).isNull();
    }

    @Test
    void taskOperationsFailWhenTaskDoesNotBelongToOwnerOrDoesNotExist() {
        when(taskRepository.findByIdAndOwnerEmail(7L, "user@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getTask("user@example.com", 7L))
                .isInstanceOf(TaskNotFoundException.class);
        verifyNoInteractions(taskMapper);
    }

    private Task task(String email) {
        return new Task("title", "body", new User(email, "encoded"));
    }
}
