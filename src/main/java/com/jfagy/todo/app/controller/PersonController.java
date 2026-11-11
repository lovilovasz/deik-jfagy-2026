package com.jfagy.todo.app.controller;

import com.jfagy.todo.app.dto.PersonRequest;
import com.jfagy.todo.app.dto.PersonResponse;
import com.jfagy.todo.app.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/persons", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Persons")
@RequiredArgsConstructor
public class PersonController {

	private final PersonService service;

	@PostMapping
	@Operation(operationId = "createPerson", summary = "Create a person")
	@ApiResponse(responseCode = "201", description = "Created")
	@ApiResponse(responseCode = "400", description = "Validation failed", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
	@ApiResponse(responseCode = "409", description = "Email already in use", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
	public ResponseEntity<PersonResponse> create(@Valid @RequestBody PersonRequest req) {
		PersonResponse body = PersonResponse.from(service.create(req));
		return ResponseEntity.created(URI.create("/api/persons/" + body.id())).body(body);
	}

	@GetMapping
	@Operation(operationId = "listPersons", summary = "List all persons")
	public List<PersonResponse> list() {
		return service.findAll().stream().map(PersonResponse::from).toList();
	}

	@GetMapping("/{id}")
	@Operation(operationId = "getPerson", summary = "Get a person by id")
	@ApiResponse(responseCode = "200", description = "OK")
	@ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
	public PersonResponse get(@PathVariable Long id) {
		return PersonResponse.from(service.get(id));
	}

	@PutMapping("/{id}")
	@Operation(operationId = "updatePerson", summary = "Update a person")
	@ApiResponse(responseCode = "200", description = "OK")
	@ApiResponse(responseCode = "400", description = "Validation failed", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
	@ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
	@ApiResponse(responseCode = "409", description = "Email already in use", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
	public PersonResponse update(@PathVariable Long id, @Valid @RequestBody PersonRequest req) {
		return PersonResponse.from(service.update(id, req));
	}

	@DeleteMapping("/{id}")
	@Operation(operationId = "deletePerson", summary = "Delete a person and their todos")
	@ApiResponse(responseCode = "204", description = "Deleted")
	@ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
