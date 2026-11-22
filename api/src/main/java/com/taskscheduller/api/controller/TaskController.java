package com.taskscheduller.api.controller;

import com.taskscheduller.api.dto.request.CreateTaskRequest;
import com.taskscheduller.api.dto.response.TaskResponse;
import com.taskscheduller.api.controller.docs.TaskControllerDocs;
import com.taskscheduller.api.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/tasks")
@RequiredArgsConstructor
public class TaskController implements TaskControllerDocs {
	private final TaskService taskService;

	@PostMapping
	public TaskResponse createTask(Authentication authentication, @Valid @RequestBody CreateTaskRequest request) {
		return taskService.createTask(ownerEmail(authentication), request.title(), request.body());
	}

	@GetMapping("/{taskId}")
	public TaskResponse getTask(Authentication authentication, @PathVariable Long taskId) {
		return taskService.getTask(ownerEmail(authentication), taskId);
	}

	@DeleteMapping("/{taskId}")
	public void deleteTask(Authentication authentication, @PathVariable Long taskId) {
		taskService.deleteTask(ownerEmail(authentication), taskId);
	}

	@PatchMapping("/{taskId}/complete")
	public TaskResponse complete(Authentication authentication, @PathVariable Long taskId) {
		return taskService.complete(ownerEmail(authentication), taskId);
	}

	@PatchMapping("/{taskId}/reopen")
	public TaskResponse reopen(Authentication authentication, @PathVariable Long taskId) {
		return taskService.reopen(ownerEmail(authentication), taskId);
	}

	private String ownerEmail(Authentication authentication) {
		return String.valueOf(authentication.getName());
	}
}
