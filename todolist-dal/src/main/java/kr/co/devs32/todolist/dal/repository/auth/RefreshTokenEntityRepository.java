package kr.co.devs32.todolist.dal.repository.auth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.devs32.todolist.dal.entity.auth.RefreshTokenEntity;

public interface RefreshTokenEntityRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByUserId(Long userId);
    Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);
}
