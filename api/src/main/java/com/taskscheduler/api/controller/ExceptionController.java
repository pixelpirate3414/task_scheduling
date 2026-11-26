package com.taskscheduler.api.controller;

import com.taskscheduler.api.dto.response.ExceptionResponse;
import com.taskscheduler.api.exception.InvalidTaskStateException;
import com.taskscheduler.api.exception.TaskNotFoundException;
import com.taskscheduler.api.exception.UserAlreadyRegisteredException;
import com.taskscheduler.api.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {
	@ExceptionHandler(TaskNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleTaskNotFound(
			TaskNotFoundException exception, HttpServletRequest request) {
		return response(HttpStatus.NOT_FOUND, exception.getMessage(), request);
	}

	@ExceptionHandler(InvalidTaskStateException.class)
	public ResponseEntity<ExceptionResponse> handleInvalidTaskState(
			InvalidTaskStateException exception, HttpServletRequest request) {
		return response(HttpStatus.CONFLICT, exception.getMessage(), request);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ExceptionResponse> handleDataIntegrityViolation(
			InvalidTaskStateException exception, HttpServletRequest request) {
		return response(HttpStatus.CONFLICT, exception.getMessage(), request);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleUserNotFound(
			UserNotFoundException exception, HttpServletRequest request) {
		return response(HttpStatus.NOT_FOUND, exception.getMessage(), request);
	}

	@ExceptionHandler(UserAlreadyRegisteredException.class)
	public ResponseEntity<ExceptionResponse> handleUserAlreadyRegistered(
			UserAlreadyRegisteredException exception, HttpServletRequest request) {
		return response(HttpStatus.CONFLICT, exception.getMessage(), request);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ExceptionResponse> handleConstraintViolation(
			ConstraintViolationException exception, HttpServletRequest request) {
		return response(HttpStatus.BAD_REQUEST, exception.getMessage(), request);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValid(
			MethodArgumentNotValidException exception, HttpServletRequest request) {
		String details = exception.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getField() + ": " + error.getDefaultMessage())
				.reduce((first, second) -> first + "; " + second)
				.orElse("Request validation failed");
		return response(HttpStatus.BAD_REQUEST, details, request);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponse> handleException(HttpServletRequest request) {
		return response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", request);
	}

	private ResponseEntity<ExceptionResponse> response(
			HttpStatus status, String details, HttpServletRequest request) {
		ExceptionResponse body = new ExceptionResponse(
				status.value(), status.getReasonPhrase(), details, request.getRequestURI());
		return ResponseEntity.status(status).body(body);
	}
}
