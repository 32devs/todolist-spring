package kr.co.devs32.todolist.dal.jpa.refreshtoken;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenJpaEntityRepository extends JpaRepository<RefreshTokenJpaEntity, Long> {
    Optional<RefreshTokenJpaEntity> findByUserId(Long userId);
    Optional<RefreshTokenJpaEntity> findByRefreshToken(String refreshToken);
}
