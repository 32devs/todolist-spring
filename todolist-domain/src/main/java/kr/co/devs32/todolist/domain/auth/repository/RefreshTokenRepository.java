package kr.co.devs32.todolist.domain.auth.repository;

import java.util.Optional;

import kr.co.devs32.todolist.domain.auth.domain.RefreshToken;

public interface RefreshTokenRepository {
	RefreshToken persist(RefreshToken token);
	Optional<RefreshToken> findByToken(String token);
}
