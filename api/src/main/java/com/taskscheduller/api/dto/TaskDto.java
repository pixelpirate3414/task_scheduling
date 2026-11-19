package com.taskscheduller.api.dto;

import com.taskscheduller.api.entity.TaskStatus;

public record TaskDto(
		String title,
		String body,
		TaskStatus status
) {
}
