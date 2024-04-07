package kr.co.devs32.todolist.web.auth;

import java.util.*;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import kr.co.devs32.todolist.domain.auth.domain.User;
import kr.co.devs32.todolist.domain.auth.service.UserAuthUseCases;
import kr.co.devs32.todolist.domain.auth.service.UserUseCases;
import kr.co.devs32.todolist.web.auth.model.AuthRole;
import kr.co.devs32.todolist.web.auth.model.UserAuthenticationToken;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
	public static final String ACCESS_TOKEN_HEADER_NAME = "todo-at";
	public static final String REFRESH_TOKEN_HEADER_NAME = "todo-rt";
	public static final String CLAIM_USER_ID = "userId";
	public static final String CLAIM_ROLE = "role";
	private final JwtProperties jwtProperties;

	@Value("${jwt.accessToken-validity-in-seconds}")
	private long accessTokenTime;
	@Value("${jwt.refreshToken-validity-in-seconds}")
	private long refreshTokenTime;

	private final UserUseCases userUseCases;
	private final UserAuthUseCases userAuthUseCases;

	public String generateAccessToken(User user) {
		return generateAccessToken(user, accessTokenTime * 1000);
	}

	public String generateAccessToken(User user, long expiry) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(CLAIM_USER_ID, user.getId());
		claims.put(CLAIM_ROLE, AuthRole.USER.name());
		return generateToken(claims, expiry);
	}

	public String generateRefreshToken(User user) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(CLAIM_USER_ID, user.getId());
		claims.put(CLAIM_ROLE, AuthRole.USER.name());
		return generateToken(claims, refreshTokenTime * 1000);
	}

	// 인증
	public Authentication getAuthentication(String token) {
		Claims claims = getClaims(token);
		Long id = (Long)claims.get(CLAIM_USER_ID);
		User user = userUseCases.findById(id);
		AuthRole role = AuthRole.valueOf((String)claims.get(CLAIM_ROLE));
		Set<SimpleGrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
		return new UserAuthenticationToken(user, authorities);
	}

	public Claims getClaims(String token) {
		return Jwts.parser()
			.verifyWith(getSigningKey())
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}

	private String generateToken(Map<String, Object> claims, long expiredAt) {
		Date now = new Date();
		Date expiration = new Date(now.getTime() + expiredAt);
		return Jwts.builder()
			.issuer(jwtProperties.getIssuer())
			.issuedAt(now)
			.expiration(expiration)
			.claims(claims)
			.signWith(getSigningKey())
			.compact();
	}

	private SecretKey getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecretKey());
		return Keys.hmacShaKeyFor(keyBytes);
	}

}
