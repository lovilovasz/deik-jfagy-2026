package com.jfagy.todo.app.repository;

import com.jfagy.todo.app.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {

	boolean existsByEmail(String email);
}
