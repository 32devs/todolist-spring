package kr.co.devs32.todolist.web.auth.utils;

import org.springframework.security.core.context.SecurityContextHolder;

import kr.co.devs32.todolist.domain.auth.domain.User;
import kr.co.devs32.todolist.web.auth.model.UserAuthenticationToken;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtils {

	public static User getUser() {
		if (SecurityContextHolder.getContext().getAuthentication() instanceof UserAuthenticationToken) {
			UserAuthenticationToken token = (UserAuthenticationToken)SecurityContextHolder.getContext()
				.getAuthentication()
				.getPrincipal();
			return token.getUser();
		} else {
			throw new IllegalStateException("not support authentication");
		}
	}
}
