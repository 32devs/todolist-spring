package kr.co.devs32.todolist.domain.auth.usecase;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import kr.co.devs32.todolist.domain.auth.domain.User;
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
				throw new IllegalArgumentException("exist user");
			});
		return userUseCases.persist(user).getId();
	}

	@Override
	public User signIn(String email, String password) {
		User user = userUseCases.findByEmail(email)
			.orElseThrow(() -> new NoSuchElementException("user not found"));

		if (user.isMatchPassword(password)) {
			throw new IllegalStateException("password not match");
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
}
