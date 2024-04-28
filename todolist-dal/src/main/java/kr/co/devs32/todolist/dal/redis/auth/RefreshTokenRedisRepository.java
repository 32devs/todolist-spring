package kr.co.devs32.todolist.dal.redis.auth;

import static kr.co.devs32.todolist.dal.redis.auth.RedisAuthConstant.TTL_REVOKE_TOKEN;
import static kr.co.devs32.todolist.dal.redis.auth.RedisAuthConstant.getKeyRevokeRefreshToken;

import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import io.micrometer.common.util.StringUtils;
import kr.co.devs32.todolist.domain.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRedisRepository implements RefreshTokenRepository {

	private final RedisTemplate<String, String> redisStringTemplate;

	@Override
	public void revokeToken(String token) {
		redisStringTemplate.opsForValue()
			.set(getKeyRevokeRefreshToken(token), token, TTL_REVOKE_TOKEN);
	}

	@Override
	public Optional<String> getRevokedToken(String token) {
		String revokeToken = redisStringTemplate.opsForValue().get(getKeyRevokeRefreshToken(token));
		if(StringUtils.isEmpty(revokeToken)) {
			return Optional.empty();
		}
		return Optional.of(revokeToken);
	}
}
