package kr.co.devs32.todolist.web.security.model;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import kr.co.devs32.todolist.domain.auth.domain.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = false)
public class UserAuthenticationToken extends AbstractAuthenticationToken {
	private final User user;

	public UserAuthenticationToken(User user, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.user = user;
		setDetails(user);
	}

	@Override
	public Object getCredentials() {
		return user.getEmail();
	}

	@Override
	public Object getPrincipal() {
		return user.getPassword();
	}
}
