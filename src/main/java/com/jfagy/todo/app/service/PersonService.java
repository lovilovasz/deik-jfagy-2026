package com.jfagy.todo.app.service;

import com.jfagy.todo.app.dto.PersonRequest;
import com.jfagy.todo.app.entity.Person;
import com.jfagy.todo.app.exception.ConflictException;
import com.jfagy.todo.app.exception.NotFoundException;
import com.jfagy.todo.app.repository.PersonRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Business logic for persons. */
@Service
@RequiredArgsConstructor
@Transactional
public class PersonService {

  private final PersonRepository repository;

  /** Creates a person; fails with 409 if the email is taken. */
  public Person create(PersonRequest req) {
    if (repository.existsByEmail(req.email())) {
      throw new ConflictException("Email already in use: " + req.email());
    }
    Person p = new Person();
    p.setName(req.name());
    p.setEmail(req.email());
    return repository.save(p);
  }

  /** Returns all persons. */
  @Transactional(readOnly = true)
  public List<Person> findAll() {
    return repository.findAll();
  }

  /** Returns a person or fails with 404. */
  @Transactional(readOnly = true)
  public Person get(Long id) {
    return repository.findById(id).orElseThrow(() -> new NotFoundException("Person", id));
  }

  /** Updates a person; fails with 404 or 409. */
  public Person update(Long id, PersonRequest req) {
    Person p = get(id);
    if (!p.getEmail().equals(req.email()) && repository.existsByEmail(req.email())) {
      throw new ConflictException("Email already in use: " + req.email());
    }
    p.setName(req.name());
    p.setEmail(req.email());
    return p;
  }

  /** Deletes a person and their todos. */
  public void delete(Long id) {
    repository.delete(get(id));
  }
}
