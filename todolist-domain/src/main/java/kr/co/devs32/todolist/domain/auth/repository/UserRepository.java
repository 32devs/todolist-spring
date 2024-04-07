package kr.co.devs32.todolist.domain.auth.repository;

import java.util.Optional;

import kr.co.devs32.todolist.domain.auth.domain.User;

public interface UserRepository {

	User persist(User user);

	Optional<User> get(Long id);

	Optional<User> findByEmail(String email);
}
