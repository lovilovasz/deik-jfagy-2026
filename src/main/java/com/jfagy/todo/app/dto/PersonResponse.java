package com.jfagy.todo.app.dto;

import com.jfagy.todo.app.entity.Person;

/**
 * Response representation of a person.
 *
 * @param id person id
 * @param name person name
 * @param email email address
 */
public record PersonResponse(Long id, String name, String email) {

  /**
   * Maps a person entity to its response.
   *
   * @param p the entity
   * @return the response
   */
  public static PersonResponse from(Person p) {
    return new PersonResponse(p.getId(), p.getName(), p.getEmail());
  }
}
