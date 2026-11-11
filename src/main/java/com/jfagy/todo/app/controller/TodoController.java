package com.jfagy.todo.app.controller;

import com.jfagy.todo.app.dto.TodoRequest;
import com.jfagy.todo.app.dto.TodoResponse;
import com.jfagy.todo.app.service.TodoService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {

	private final TodoService service;

	@PostMapping
	public ResponseEntity<TodoResponse> create(@Valid @RequestBody TodoRequest req) {
		TodoResponse body = TodoResponse.from(service.create(req));
		return ResponseEntity.created(URI.create("/api/todos/" + body.id())).body(body);
	}

	@GetMapping
	public List<TodoResponse> list(@RequestParam(required = false) Long personId) {
		return service.findAll(personId).stream().map(TodoResponse::from).toList();
	}

	@GetMapping("/{id}")
	public TodoResponse get(@PathVariable Long id) {
		return TodoResponse.from(service.get(id));
	}

	@PutMapping("/{id}")
	public TodoResponse update(@PathVariable Long id, @Valid @RequestBody TodoRequest req) {
		return TodoResponse.from(service.update(id, req));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
