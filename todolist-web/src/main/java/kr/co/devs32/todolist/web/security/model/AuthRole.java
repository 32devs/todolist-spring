package kr.co.devs32.todolist.web.security.model;

import lombok.Getter;

@Getter
public enum AuthRole {
	USER("ROLE_USER");

	private final String role;

	AuthRole(String role) {
		this.role = role;
	}
}
