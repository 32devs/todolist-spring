package kr.co.devs32.todolist.domain.auth.repository;

import java.util.Optional;

import kr.co.devs32.todolist.domain.auth.domain.User;

public interface UserRepository {

	// 저장
	User persist(User user);

	// 조회
	Optional<User> get(Long id);

	// 이메일로 조회
	Optional<User> findByEmail(String email);
}
