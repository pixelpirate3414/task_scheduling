package com.taskscheduller.api.dto;

import java.util.List;

public record UserDto(
		String email,
		List<TaskDto> tasks
) {
}
