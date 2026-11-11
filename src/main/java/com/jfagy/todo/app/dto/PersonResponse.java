package com.jfagy.todo.app.dto;

import com.jfagy.todo.app.entity.Person;

public record PersonResponse(Long id, String name, String email) {

	public static PersonResponse from(Person p) {
		return new PersonResponse(p.getId(), p.getName(), p.getEmail());
	}
}
