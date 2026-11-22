package com.taskscheduller.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTaskRequest(
		@NotBlank
		@Size(max = 255)
		String title,
		@Size(max = 2048)
		String body
) {
}
