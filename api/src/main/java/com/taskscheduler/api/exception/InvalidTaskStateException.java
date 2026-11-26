package com.taskscheduler.api.exception;

public class InvalidTaskStateException extends RuntimeException {
	public InvalidTaskStateException(String message) {
		super(message);
	}
}
