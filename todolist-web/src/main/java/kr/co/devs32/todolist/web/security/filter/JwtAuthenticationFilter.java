package kr.co.devs32.todolist.web.security.filter;

import static kr.co.devs32.todolist.web.security.token.jwt.JwtProvider.ACCESS_TOKEN_HEADER_NAME;
import static kr.co.devs32.todolist.web.security.token.jwt.JwtProvider.REFRESH_TOKEN_HEADER_NAME;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.devs32.todolist.domain.auth.domain.User;
import kr.co.devs32.todolist.web.security.token.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtProvider jwtProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request,
		@NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
		String accessToken = request.getHeader(ACCESS_TOKEN_HEADER_NAME);
		String refreshToken = request.getHeader(REFRESH_TOKEN_HEADER_NAME);

		if (StringUtils.isNotEmpty(accessToken)) {
			try {
				jwtProvider.validateAccessToken(accessToken);
				Authentication authentication = jwtProvider.getAuthentication(accessToken);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} catch (ExpiredJwtException jwtException) {
				// 엑세스토큰이 만료되었을때, refresh-token 이 유효하다면 access-token 재발급
				if (StringUtils.isNotEmpty(refreshToken)) {
					generateNewAccessToken(refreshToken, response);
				}
			}
		}
		filterChain.doFilter(request, response);
	}

	private void generateNewAccessToken(String refreshToken, HttpServletResponse response) {
		jwtProvider.validateRefreshToken(refreshToken);
		Authentication authentication = jwtProvider.getAuthentication(refreshToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String newAccessToken = jwtProvider.generateAccessToken((User)authentication.getPrincipal());
		response.setHeader(ACCESS_TOKEN_HEADER_NAME, newAccessToken);
	}
}
