package com.jfagy.todo.app.controller;

import com.jfagy.todo.app.dto.TodoRequest;
import com.jfagy.todo.app.dto.TodoResponse;
import com.jfagy.todo.app.service.TodoService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** REST endpoints for todos. */
@RestController
@RequestMapping(path = "/api/todos", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Todos")
@RequiredArgsConstructor
public class TodoController {

  private final TodoService service;

  /** Creates a todo. */
  @PostMapping
  @Operation(operationId = "createTodo", summary = "Create a todo for a person")
  @ApiResponse(responseCode = "201", description = "Created")
  @ApiResponse(
      responseCode = "400",
      description = "Validation failed",
      content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
  @ApiResponse(
      responseCode = "404",
      description = "Person not found",
      content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
  public ResponseEntity<TodoResponse> create(@Valid @RequestBody TodoRequest req) {
    TodoResponse body = TodoResponse.from(service.create(req));
    return ResponseEntity.created(URI.create("/api/todos/" + body.id())).body(body);
  }

  /** Lists todos, optionally filtered by person. */
  @GetMapping
  @Operation(operationId = "listTodos", summary = "List todos, optionally filtered by person")
  public List<TodoResponse> list(@RequestParam(required = false) Long personId) {
    return service.findAll(personId).stream().map(TodoResponse::from).toList();
  }

  /** Returns one todo. */
  @GetMapping("/{id}")
  @Operation(operationId = "getTodo", summary = "Get a todo by id")
  @ApiResponse(responseCode = "200", description = "OK")
  @ApiResponse(
      responseCode = "404",
      description = "Not found",
      content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
  public TodoResponse get(@PathVariable Long id) {
    return TodoResponse.from(service.get(id));
  }

  /** Updates a todo. */
  @PutMapping("/{id}")
  @Operation(operationId = "updateTodo", summary = "Update a todo")
  @ApiResponse(responseCode = "200", description = "OK")
  @ApiResponse(
      responseCode = "400",
      description = "Validation failed",
      content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
  @ApiResponse(
      responseCode = "404",
      description = "Todo or person not found",
      content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
  public TodoResponse update(@PathVariable Long id, @Valid @RequestBody TodoRequest req) {
    return TodoResponse.from(service.update(id, req));
  }

  /** Deletes a todo. */
  @DeleteMapping("/{id}")
  @Operation(operationId = "deleteTodo", summary = "Delete a todo")
  @ApiResponse(responseCode = "204", description = "Deleted")
  @ApiResponse(
      responseCode = "404",
      description = "Not found",
      content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
