package kr.co.devs32.todolist.domain.auth.service;

import java.util.Optional;

import kr.co.devs32.todolist.domain.auth.domain.User;

public interface UserUseCases {

	// 이메일로 회원 조회
	User persist(User user);

	// 이메일로 회원 조회
	Optional<User> findByEmail(String email);

	// 이메일로 회원 조회
	User get(Long id);
}
