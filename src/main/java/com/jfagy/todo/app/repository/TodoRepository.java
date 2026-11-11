package com.jfagy.todo.app.repository;

import com.jfagy.todo.app.entity.Todo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {

	List<Todo> findByPersonId(Long personId);
}
