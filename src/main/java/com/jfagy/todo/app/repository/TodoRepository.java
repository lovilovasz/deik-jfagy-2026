package com.jfagy.todo.app.repository;

import com.jfagy.todo.app.entity.Todo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/** Persistence for todos. */
public interface TodoRepository extends JpaRepository<Todo, Long> {

  /**
   * Finds the todos of a person.
   *
   * @param personId owner id
   * @return the todos
   */
  List<Todo> findByPersonId(Long personId);
}
