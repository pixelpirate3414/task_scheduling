package com.taskscheduller.api.dto.response;

import com.taskscheduller.api.entity.TaskStatus;

public record TaskResponse(
        String title,
        String body,
        TaskStatus status
) {
}
