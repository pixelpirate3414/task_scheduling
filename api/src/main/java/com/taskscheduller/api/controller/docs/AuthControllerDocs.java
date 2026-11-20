package com.taskscheduller.api.controller.docs;

import com.taskscheduller.api.dto.request.AuthRequest;
import com.taskscheduller.api.dto.response.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Авторизация", description = "Вход пользователя")
public interface AuthControllerDocs {

	@Operation(summary = "Войти", description = "Проверяет учетные данные и возвращает JWT")
	@ApiResponse(responseCode = "200", description = "Успешная авторизация",
			content = @Content(schema = @Schema(implementation = AuthResponse.class)))
	@ApiResponse(responseCode = "400", description = "Некорректный запрос", content = @Content)
	@ApiResponse(responseCode = "401", description = "Неверные учетные данные", content = @Content)
	@PostMapping("/login")
	AuthResponse login(@Valid @RequestBody AuthRequest request);
}
