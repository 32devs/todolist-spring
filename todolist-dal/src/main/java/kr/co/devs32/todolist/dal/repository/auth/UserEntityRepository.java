package kr.co.devs32.todolist.dal.repository.auth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.devs32.todolist.dal.entity.auth.UserEntity;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email); // email로 사용자 정보를 가져옴
}
