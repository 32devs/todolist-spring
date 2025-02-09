package kr.co.devs32.todolist.web.security.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import kr.co.devs32.todolist.domain.auth.domain.User;
import lombok.Getter;

@Getter
public class AuthUser implements UserDetails {
	private final User detail;

	public AuthUser(User detail) {
		this.detail = detail;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(AuthRole.USER.getRole()));
	}

	@Override
	public String getPassword() {
		return detail.getPassword();
	}

	@Override
	public String getUsername() {
		return detail.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return false;
	}
}
