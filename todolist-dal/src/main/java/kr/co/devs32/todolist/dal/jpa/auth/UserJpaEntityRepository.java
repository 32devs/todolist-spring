package kr.co.devs32.todolist.dal.jpa.auth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaEntityRepository extends JpaRepository<UserJpaEntity, Long> {
    Optional<UserJpaEntity> findByEmail(String email);
}
