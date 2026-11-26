package com.taskscheduler.api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ExceptionResponse(
		int status,
		String message,
		String details,
		String url,
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		LocalDateTime timestamp
) {
	public ExceptionResponse(int status, String message, String details, String url) {
		this(status, message, details, url, LocalDateTime.now());
	}
}
