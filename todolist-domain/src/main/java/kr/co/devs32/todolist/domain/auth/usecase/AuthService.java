package kr.co.devs32.todolist.domain.auth.usecase;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import kr.co.devs32.todolist.common.error.TodolistException;
import kr.co.devs32.todolist.domain.auth.domain.User;
import kr.co.devs32.todolist.domain.auth.error.AuthErrorCode;
import kr.co.devs32.todolist.domain.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthService implements AuthUseCases {

	private final UserUseCases userUseCases;
	private final RefreshTokenRepository refreshTokenRepository;

	@Override
	public Long signUp(User user) {
		userUseCases.findByEmail(user.getEmail())
			.ifPresent(e -> {
				throw buildDuplicatedUserException();
			});
		return userUseCases.persist(user).getId();
	}

	@Override
	public User signIn(String email, String password) {
		User user = userUseCases.findByEmail(email)
			.orElseThrow(AuthService::buildNotFoundUserException);

		if (!user.isMatchPassword(password)) {
			throw buildInvalidLoginException();
		}

		return user;
	}

	@Override
	public Optional<String> findByRefreshToken(String token) {
		return refreshTokenRepository.getRevokedToken(token);
	}

	@Override
	public void revokeRefreshToken(String token) {
		refreshTokenRepository.revokeToken(token);
	}

	private static TodolistException buildNotFoundUserException() {
		return TodolistException.builder()
			.errorCode(AuthErrorCode.NOT_FOUND_USER)
			.httpStatus(HttpStatus.BAD_REQUEST)
			.build();
	}

	private static TodolistException buildDuplicatedUserException() {
		return TodolistException.builder()
			.errorCode(AuthErrorCode.DUPLICATED_USER)
			.httpStatus(HttpStatus.BAD_REQUEST)
			.build();
	}

	private static TodolistException buildInvalidLoginException() {
		return TodolistException.builder()
			.errorCode(AuthErrorCode.INVALID_LOGIN)
			.httpStatus(HttpStatus.BAD_REQUEST)
			.build();
	}
}
