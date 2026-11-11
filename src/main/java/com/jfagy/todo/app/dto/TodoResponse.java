package com.jfagy.todo.app.dto;

import com.jfagy.todo.app.entity.Todo;

public record TodoResponse(Long id, String title, String description, boolean completed, Long personId) {

	public static TodoResponse from(Todo t) {
		return new TodoResponse(t.getId(), t.getTitle(), t.getDescription(), t.isCompleted(), t.getPerson().getId());
	}
}
