package com.taskscheduler.summarization.dto.request;

import java.util.List;

public record SummarizeRequest(
        String user,
        Tasks tasks
) {
    public record Tasks(
            List<Task> finished,
            List<Task> unfinished
    ) {
    }

    public record Task(
            String title,
            String body,
            String status
    ) {
    }
}
