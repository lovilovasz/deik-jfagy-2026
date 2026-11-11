package com.jfagy.todo.app.controller;

import com.jfagy.todo.app.dto.PersonRequest;
import com.jfagy.todo.app.dto.PersonResponse;
import com.jfagy.todo.app.service.PersonService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/persons")
@RequiredArgsConstructor
public class PersonController {

	private final PersonService service;

	@PostMapping
	public ResponseEntity<PersonResponse> create(@Valid @RequestBody PersonRequest req) {
		PersonResponse body = PersonResponse.from(service.create(req));
		return ResponseEntity.created(URI.create("/api/persons/" + body.id())).body(body);
	}

	@GetMapping
	public List<PersonResponse> list() {
		return service.findAll().stream().map(PersonResponse::from).toList();
	}

	@GetMapping("/{id}")
	public PersonResponse get(@PathVariable Long id) {
		return PersonResponse.from(service.get(id));
	}

	@PutMapping("/{id}")
	public PersonResponse update(@PathVariable Long id, @Valid @RequestBody PersonRequest req) {
		return PersonResponse.from(service.update(id, req));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
