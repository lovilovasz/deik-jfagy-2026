package com.jfagy.todo.app.exception;

/** Thrown when a request conflicts with existing data (HTTP 409). */
public class ConflictException extends RuntimeException {

  /**
   * Creates the exception.
   *
   * @param message detail message
   */
  public ConflictException(String message) {
    super(message);
  }
}
