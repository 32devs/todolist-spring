package kr.co.devs32.todolist.domain.auth.domain;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.Getter;
import lombok.Setter;

@Getter
public class User {
	@Setter
	private Long id;
	private String email;
	private String password;

	public User(String email, String password) {
		this.email = email;
		setPassword(password);
	}

	public void setPassword(String password) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		this.password = encoder.encode(password);
	}

	public boolean isMatchPassword(String password) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.matches(password, this.password);
	}
}
