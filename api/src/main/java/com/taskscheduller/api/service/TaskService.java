package com.taskscheduller.api.service;

import com.taskscheduller.api.dto.response.TaskResponse;

public interface TaskService {
	TaskResponse createTask(String ownerEmail, String title, String body);
	TaskResponse getTask(String ownerEmail, Long taskId);
	void deleteTask(String ownerEmail, Long taskId);
	TaskResponse complete(String ownerEmail, Long taskId);
	TaskResponse reopen(String ownerEmail, Long taskId);
}
