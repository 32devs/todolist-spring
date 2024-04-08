package kr.co.devs32.todolist.web.auth.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kr.co.devs32.todolist.common.request.auth.AddUserRequest;
import kr.co.devs32.todolist.domain.auth.domain.User;
import kr.co.devs32.todolist.domain.auth.service.AuthUseCases;
import kr.co.devs32.todolist.web.auth.JwtProvider;
import kr.co.devs32.todolist.web.auth.request.SignInRequest;
import kr.co.devs32.todolist.web.auth.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthUseCases authUseCases;
	private final JwtProvider tokenProvider;

	//회원가입
	@PostMapping("/signUp")
	public ResponseEntity<Long> signUp(@Valid @RequestBody AddUserRequest request) {
		User user = new User(request.getEmail(), request.getPassword());
		return ResponseEntity.ok(authUseCases.signUp(user));
	}

	//로그인
	@PostMapping("/signIn")
	public ResponseEntity<Void> signIn(@RequestBody SignInRequest request, HttpServletResponse response) {
		User user = authUseCases.signIn(request.getEmail(), request.getPassword());
		response.setHeader(JwtProvider.ACCESS_TOKEN_HEADER_NAME, tokenProvider.generateAccessToken(user));
		response.setHeader(JwtProvider.ACCESS_TOKEN_HEADER_NAME, tokenProvider.generateRefreshToken(user));
		return ResponseEntity.ok(null);
	}

	//로그아웃
	@PostMapping("/logout")
	public ResponseEntity<Void> logout(
		@RequestHeader(JwtProvider.ACCESS_TOKEN_HEADER_NAME) String accessToken
		, @RequestHeader(JwtProvider.REFRESH_TOKEN_HEADER_NAME) String refreshToken
		, HttpServletResponse response) {
		User user = SecurityUtils.getUser();

		// 엑세스 토큰 만료 처리
		response.setHeader(JwtProvider.ACCESS_TOKEN_HEADER_NAME, tokenProvider.generateAccessToken(user, 0));

		// 리프레시토큰 블랙리스트 처리
		authUseCases.revokeRefreshToken(user.getId(), refreshToken);
		return ResponseEntity.ok(null);
	}

}
