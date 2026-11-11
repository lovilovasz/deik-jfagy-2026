package com.jfagy.todo.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TodoRequest(@NotBlank String title, String description, Boolean completed, @NotNull Long personId) {
}
