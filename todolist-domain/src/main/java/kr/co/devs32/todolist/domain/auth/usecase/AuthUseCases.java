package kr.co.devs32.todolist.domain.auth.usecase;

import java.util.Optional;

import kr.co.devs32.todolist.domain.auth.domain.RefreshToken;
import kr.co.devs32.todolist.domain.auth.domain.User;

public interface AuthUseCases {

	// 회원가입
	Long signUp(User user);

	// 로그인
	User signIn(String email, String password);

	// 리프레시 토큰조회
	Optional<RefreshToken> findByRefreshToken(String token);

	// 리프레시 토큰 폐기
	void revokeRefreshToken(Long userId, String token);
}
