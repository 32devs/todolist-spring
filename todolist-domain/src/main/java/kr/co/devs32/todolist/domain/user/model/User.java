package kr.co.devs32.todolist.domain.user.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class User {

	private Long id;

	private String email;

	private String password;
}
