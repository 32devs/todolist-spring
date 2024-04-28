package kr.co.devs32.todolist.domain.auth.repository;

import java.util.Optional;

public interface RefreshTokenRepository {
	// 저장
	void revokeToken(String token);

	// 조회
	Optional<String> getRevokedToken(String token);
}
