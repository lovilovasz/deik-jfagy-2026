package com.jfagy.todo.app.service;

import com.jfagy.todo.app.dto.TodoRequest;
import com.jfagy.todo.app.entity.Todo;
import com.jfagy.todo.app.exception.NotFoundException;
import com.jfagy.todo.app.repository.TodoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TodoService {

	private final TodoRepository repository;
	private final PersonService personService;

	public Todo create(TodoRequest req) {
		Todo t = new Todo();
		apply(t, req);
		return repository.save(t);
	}

	@Transactional(readOnly = true)
	public List<Todo> findAll(Long personId) {
		return personId == null ? repository.findAll() : repository.findByPersonId(personId);
	}

	@Transactional(readOnly = true)
	public Todo get(Long id) {
		return repository.findById(id).orElseThrow(() -> new NotFoundException("Todo", id));
	}

	public Todo update(Long id, TodoRequest req) {
		Todo t = get(id);
		apply(t, req);
		return t;
	}

	public void delete(Long id) {
		repository.delete(get(id));
	}

	private void apply(Todo t, TodoRequest req) {
		t.setTitle(req.title());
		t.setDescription(req.description());
		t.setCompleted(Boolean.TRUE.equals(req.completed()));
		t.setPerson(personService.get(req.personId()));
	}
}
