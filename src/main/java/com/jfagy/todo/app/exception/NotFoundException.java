package com.jfagy.todo.app.exception;

/** Thrown when an entity does not exist (HTTP 404). */
public class NotFoundException extends RuntimeException {

  /**
   * Creates the exception.
   *
   * @param entity entity name
   * @param id missing id
   */
  public NotFoundException(String entity, Long id) {
    super(entity + " not found: " + id);
  }
}
