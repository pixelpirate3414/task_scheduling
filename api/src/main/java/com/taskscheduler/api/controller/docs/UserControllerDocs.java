package com.taskscheduler.api.controller.docs;

import com.taskscheduler.api.dto.request.AuthRequest;
import com.taskscheduler.api.dto.response.AuthResponse;
import com.taskscheduler.api.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Пользователь", description = "Регистрация и данные текущего пользователя")
public interface UserControllerDocs {

	@Operation(summary = "Зарегистрировать пользователя")
	@ApiResponse(responseCode = "200", description = "Пользователь зарегистрирован",
			content = @Content(schema = @Schema(implementation = AuthResponse.class)))
	@ApiResponse(responseCode = "400", description = "Некорректный запрос", content = @Content)
	@ApiResponse(responseCode = "409", description = "Пользователь существует", content = @Content)
	@PostMapping
	AuthResponse register(@Valid @RequestBody AuthRequest request);

	@Operation(summary = "Получить текущего пользователя")
	@ApiResponse(responseCode = "200", description = "Данные пользователя",
			content = @Content(schema = @Schema(implementation = UserResponse.class)))
	@ApiResponse(responseCode = "401", description = "Требуется авторизация", content = @Content)
	@GetMapping
	UserResponse getCurrentUser(Authentication authentication);
}
