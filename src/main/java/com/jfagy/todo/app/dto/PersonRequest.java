package com.jfagy.todo.app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Request body for creating or updating a person.
 *
 * @param name person name
 * @param email unique email address
 */
public record PersonRequest(@NotBlank String name, @NotBlank @Email String email) {}
