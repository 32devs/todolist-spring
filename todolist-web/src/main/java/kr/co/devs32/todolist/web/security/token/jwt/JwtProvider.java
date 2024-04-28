package kr.co.devs32.todolist.web.security.token.jwt;

import java.security.*;
import java.security.spec.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import kr.co.devs32.todolist.domain.auth.domain.User;
import kr.co.devs32.todolist.domain.auth.usecase.AuthUseCases;
import kr.co.devs32.todolist.domain.auth.usecase.UserUseCases;
import kr.co.devs32.todolist.web.security.model.*;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtProvider {
	public static final String ACCESS_TOKEN_HEADER_NAME = "todo-at";
	public static final String REFRESH_TOKEN_HEADER_NAME = "todo-rt";
	public static final String CLAIM_USER_ID = "userId";
	public static final String CLAIM_ROLE = "role";
	public static final String KEY_FACTORY_ALGORITHM = "EC";
	private final JwtProperties jwtProperties;

	@Value("${jwt.accessToken-validity-in-seconds}")
	private long accessTokenTime;
	@Value("${jwt.refreshToken-validity-in-seconds}")
	private long refreshTokenTime;

	private final UserUseCases userUseCases;
	private final AuthUseCases authUseCases;

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
		Long id = claims.get(CLAIM_USER_ID, Long.class);
		AuthRole role = AuthRole.valueOf(claims.get(CLAIM_ROLE, String.class));
		AuthUser authUser;
		if(role == AuthRole.USER) {
			authUser = new AuthUser(userUseCases.get(id));
		} else {
			throw new IllegalStateException("not support role: " + role);
		}
		return new UserAuthenticationToken(authUser);
	}

	public void validateAccessToken(String token) {
		getClaims(token);
	}

	public void validateRefreshToken(String token) {
		Optional<String> optional = authUseCases.findByRefreshToken(token);
		if(optional.isPresent()) {
			// 폐기된 토큰이라면 거절
			throw new IllegalStateException("this is revoked token");
		}
		getClaims(token);
	}

	public Claims getClaims(String token) {
		return Jwts.parser()
			.verifyWith(getPublicKey())
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
			.signWith(getSigningKey(), Jwts.SIG.ES256)
			.compact();
	}

	private PrivateKey getSigningKey() {
		try {
			byte[] decodedPrivateKey = Base64.getDecoder().decode(jwtProperties.getPrivateKey());
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedPrivateKey);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_FACTORY_ALGORITHM);
			return keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new IllegalStateException("Failed to get private key", e);
		}
	}

	private PublicKey getPublicKey() {
		try {
			byte[] decodedPublicKey = Base64.getDecoder().decode(jwtProperties.getPublicKey());
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedPublicKey);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_FACTORY_ALGORITHM);
			return keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new IllegalStateException("Failed to get public key", e);
		}
	}

}
