package com.taskscheduler.api.dto.response;

import com.taskscheduler.api.entity.TaskStatus;

public record TaskResponse(
        String title,
        String body,
        TaskStatus status
) {
}
