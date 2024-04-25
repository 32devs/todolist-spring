package kr.co.devs32.todolist.web.security.model;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = false)
public class UserAuthenticationToken extends AbstractAuthenticationToken {
	private final AuthUser user;

	public UserAuthenticationToken(AuthUser user) {
		super(user.getAuthorities());
		this.user = user;
		setDetails(user);
	}

	@Override
	public Object getCredentials() {
		return user.getUsername();
	}

	@Override
	public Object getPrincipal() {
		return user.getPassword();
	}
}
