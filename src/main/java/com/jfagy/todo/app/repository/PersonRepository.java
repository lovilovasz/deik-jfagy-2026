package com.jfagy.todo.app.repository;

import com.jfagy.todo.app.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

/** Persistence for persons. */
public interface PersonRepository extends JpaRepository<Person, Long> {

  /**
   * Checks whether an email is taken.
   *
   * @param email email address
   * @return true if a person has this email
   */
  boolean existsByEmail(String email);
}
