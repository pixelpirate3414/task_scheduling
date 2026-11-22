package com.taskscheduller.api.controller.docs;

import com.taskscheduller.api.dto.request.CreateTaskRequest;
import com.taskscheduller.api.dto.response.TaskResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Задачи", description = "Создание и управление задачами текущего пользователя")
public interface TaskControllerDocs {

	@Operation(summary = "Создать задачу")
	@ApiResponse(responseCode = "200", description = "Задача создана",
			content = @Content(schema = @Schema(implementation = TaskResponse.class)))
	@ApiResponse(responseCode = "400", description = "Некорректный запрос", content = @Content)
	@ApiResponse(responseCode = "401", description = "Требуется авторизация", content = @Content)
	@PostMapping
	TaskResponse createTask(Authentication authentication, @Valid @RequestBody CreateTaskRequest request);

	@Operation(summary = "Получить задачу")
	@ApiResponse(responseCode = "200", description = "Данные задачи",
			content = @Content(schema = @Schema(implementation = TaskResponse.class)))
	@ApiResponse(responseCode = "401", description = "Требуется авторизация", content = @Content)
	@ApiResponse(responseCode = "404", description = "Задача не найдена", content = @Content)
	@GetMapping("/{taskId}")
	TaskResponse getTask(Authentication authentication, @PathVariable Long taskId);

	@Operation(summary = "Удалить задачу")
	@ApiResponse(responseCode = "200", description = "Задача удалена", content = @Content)
	@ApiResponse(responseCode = "401", description = "Требуется авторизация", content = @Content)
	@ApiResponse(responseCode = "404", description = "Задача не найдена", content = @Content)
	@DeleteMapping("/{taskId}")
	void deleteTask(Authentication authentication, @PathVariable Long taskId);

	@Operation(summary = "Завершить задачу")
	@ApiResponse(responseCode = "200", description = "Задача завершена",
			content = @Content(schema = @Schema(implementation = TaskResponse.class)))
	@ApiResponse(responseCode = "401", description = "Требуется авторизация", content = @Content)
	@ApiResponse(responseCode = "404", description = "Задача не найдена", content = @Content)
	@PatchMapping("/{taskId}/complete")
	TaskResponse complete(Authentication authentication, @PathVariable Long taskId);

	@Operation(summary = "Возобновить задачу")
	@ApiResponse(responseCode = "200", description = "Задача возобновлена",
			content = @Content(schema = @Schema(implementation = TaskResponse.class)))
	@ApiResponse(responseCode = "401", description = "Требуется авторизация", content = @Content)
	@ApiResponse(responseCode = "404", description = "Задача не найдена", content = @Content)
	@PatchMapping("/{taskId}/reopen")
	TaskResponse reopen(Authentication authentication, @PathVariable Long taskId);
}
