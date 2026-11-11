package com.jfagy.todo.app.exception;

public class NotFoundException extends RuntimeException {

	public NotFoundException(String entity, Long id) {
		super(entity + " not found: " + id);
	}
}
