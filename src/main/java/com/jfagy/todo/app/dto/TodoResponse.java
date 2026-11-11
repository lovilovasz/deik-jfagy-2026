package com.jfagy.todo.app.dto;

import com.jfagy.todo.app.entity.Todo;

/**
 * Response representation of a todo.
 *
 * @param id todo id
 * @param title todo title
 * @param description description
 * @param completed completion flag
 * @param personId id of the owning person
 */
public record TodoResponse(
    Long id, String title, String description, boolean completed, Long personId) {

  /**
   * Maps a todo entity to its response.
   *
   * @param t the entity
   * @return the response
   */
  public static TodoResponse from(Todo t) {
    return new TodoResponse(
        t.getId(), t.getTitle(), t.getDescription(), t.isCompleted(), t.getPerson().getId());
  }
}
