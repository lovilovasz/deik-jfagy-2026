package com.jfagy.todo.app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PersonRequest(@NotBlank String name, @NotBlank @Email String email) {
}
