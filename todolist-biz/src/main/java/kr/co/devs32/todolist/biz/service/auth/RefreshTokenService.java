package kr.co.devs32.todolist.biz.service.auth;

import kr.co.devs32.todolist.dal.entity.auth.RefreshTokenEntity;
import kr.co.devs32.todolist.dal.repository.auth.RefreshTokenEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenEntityRepository refreshTokenEntityRepository;

    public RefreshTokenEntity findByRefreshToken(String refreshToken) {
        return refreshTokenEntityRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }

}
