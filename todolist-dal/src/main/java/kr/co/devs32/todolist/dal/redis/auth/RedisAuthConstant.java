package kr.co.devs32.todolist.dal.redis.auth;

import java.time.Duration;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RedisAuthConstant {

	public static final Duration TTL_REVOKE_TOKEN = Duration.ofDays(7);

	public static String getKeyRevokeRefreshToken(String token) {
		return "revoke-refresh-token:" + token;
	}
}
