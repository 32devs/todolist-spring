package kr.co.devs32.todolist.dal.jpa.refreshtoken;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import kr.co.devs32.todolist.dal.mapper.RefreshTokenMapper;
import kr.co.devs32.todolist.domain.auth.domain.RefreshToken;
import kr.co.devs32.todolist.domain.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RefreshTokenJpaRepository implements RefreshTokenRepository {

    private final RefreshTokenJpaEntityRepository jpaEntityRepository;

    @Override
    public RefreshToken persist(RefreshToken token) {
        RefreshTokenJpaEntity entity = RefreshTokenMapper.INSTANCE.convert(token);
        jpaEntityRepository.save(entity);
        token.setId(entity.getId());
        return token;
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        Optional<RefreshTokenJpaEntity> optional = jpaEntityRepository.findByRefreshToken(token);
        return optional.map(RefreshTokenMapper.INSTANCE::convert);
    }
}
