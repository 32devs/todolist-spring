package kr.co.devs32.todolist.domain.auth.repository.jpa;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import kr.co.devs32.todolist.dal.jpa.refreshtoken.RefreshTokenJpaEntity;
import kr.co.devs32.todolist.dal.jpa.refreshtoken.RefreshTokenJpaEntityRepository;
import kr.co.devs32.todolist.domain.auth.domain.RefreshToken;
import kr.co.devs32.todolist.domain.auth.repository.RefreshTokenRepository;
import kr.co.devs32.todolist.domain.mapper.RefreshTokenMapper;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RefreshJpaRepository implements RefreshTokenRepository {
    private final RefreshTokenJpaEntityRepository repository;

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        Optional<RefreshTokenJpaEntity> optional = repository.findByRefreshToken(token);
        return optional.map(RefreshTokenMapper.INSTANCE::convert);
    }
}
