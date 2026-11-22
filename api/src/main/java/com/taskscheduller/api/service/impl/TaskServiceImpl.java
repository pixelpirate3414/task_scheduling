package com.taskscheduller.api.service.impl;

import com.taskscheduller.api.dto.response.TaskResponse;
import com.taskscheduller.api.entity.Task;
import com.taskscheduller.api.entity.User;
import com.taskscheduller.api.exception.TaskNotFoundException;
import com.taskscheduller.api.exception.UserNotFoundException;
import com.taskscheduller.api.mapper.TaskMapper;
import com.taskscheduller.api.repository.TaskRepository;
import com.taskscheduller.api.repository.UserRepository;
import com.taskscheduller.api.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
	private final TaskRepository taskRepository;
	private final UserRepository userRepository;
	private final TaskMapper taskMapper;

	@Override
	@Transactional
	public TaskResponse createTask(String ownerEmail, String title, String body) {
		User owner = userRepository.findByEmail(ownerEmail)
				.orElseThrow(() -> new UserNotFoundException(ownerEmail));
		Task task = new Task(title, body, owner);
		return taskMapper.toResponse(taskRepository.save(task));
	}

	@Override
	@Transactional(readOnly = true)
	public TaskResponse getTask(String ownerEmail, Long taskId) {
		return taskMapper.toResponse(findTask(ownerEmail, taskId));
	}

	@Override
	@Transactional
	public void deleteTask(String ownerEmail, Long taskId) {
		taskRepository.delete(findTask(ownerEmail, taskId));
	}

	@Override
	@Transactional
	public TaskResponse complete(String ownerEmail, Long taskId) {
		Task task = findTask(ownerEmail, taskId);
		task.complete();
		return taskMapper.toResponse(task);
	}

	@Override
	@Transactional
	public TaskResponse reopen(String ownerEmail, Long taskId) {
		Task task = findTask(ownerEmail, taskId);
		task.reopen();
		return taskMapper.toResponse(task);
	}

	private Task findTask(String ownerEmail, Long taskId) {
		return taskRepository.findByIdAndOwnerEmail(taskId, ownerEmail)
				.orElseThrow(() -> new TaskNotFoundException(taskId));
	}
}
