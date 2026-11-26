package com.taskscheduler.api.exception;

public class UserAlreadyRegisteredException extends RuntimeException {
	public UserAlreadyRegisteredException(String email) {
		super("User is already registered: " + email);
	}
}
