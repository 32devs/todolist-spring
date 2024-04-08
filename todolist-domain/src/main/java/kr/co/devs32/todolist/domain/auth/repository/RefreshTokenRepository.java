package kr.co.devs32.todolist.domain.auth.repository;

import java.util.Optional;

import kr.co.devs32.todolist.domain.auth.domain.RefreshToken;

public interface RefreshTokenRepository {
	// 저장
	RefreshToken persist(RefreshToken token);

	// 조회
	Optional<RefreshToken> findByToken(String token);
}
