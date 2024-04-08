package kr.co.devs32.todolist.web.auth.filter;

import static kr.co.devs32.todolist.web.auth.JwtProvider.ACCESS_TOKEN_HEADER_NAME;
import static kr.co.devs32.todolist.web.auth.JwtProvider.REFRESH_TOKEN_HEADER_NAME;

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
import kr.co.devs32.todolist.web.auth.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtProvider tokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request,
		@NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
		String accessToken = request.getHeader(ACCESS_TOKEN_HEADER_NAME);
		String refreshToken = request.getHeader(REFRESH_TOKEN_HEADER_NAME);

		if (StringUtils.isNotEmpty(accessToken)) {
			try {
				tokenProvider.validate(accessToken);
				Authentication authentication = tokenProvider.getAuthentication(accessToken);
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
		tokenProvider.validate(refreshToken); // TODO : 리보크된 리프레시 토큰인지 디비에서 조회
		Authentication authentication = tokenProvider.getAuthentication(refreshToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String newAccessToken = tokenProvider.generateAccessToken((User)authentication.getPrincipal());
		response.setHeader(ACCESS_TOKEN_HEADER_NAME, newAccessToken);
	}
}
