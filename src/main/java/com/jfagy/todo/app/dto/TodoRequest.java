package com.jfagy.todo.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request body for creating or updating a todo.
 *
 * @param title todo title
 * @param description optional description
 * @param completed optional completion flag
 * @param personId id of the owning person
 */
public record TodoRequest(
    @NotBlank String title, String description, Boolean completed, @NotNull Long personId) {}
