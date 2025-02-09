package kr.co.devs32.todolist.web.auth.rest;

import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kr.co.devs32.todolist.domain.auth.domain.User;
import kr.co.devs32.todolist.domain.auth.usecase.AuthUseCases;
import kr.co.devs32.todolist.web.auth.request.SignInRequest;
import kr.co.devs32.todolist.web.auth.request.SignUpRequest;
import kr.co.devs32.todolist.web.global.response.TodolistResponse;
import kr.co.devs32.todolist.web.security.model.UserAuthenticationToken;
import kr.co.devs32.todolist.web.security.token.jwt.JwtProvider;
import kr.co.devs32.todolist.web.security.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthUseCases authUseCases;
	private final JwtProvider tokenProvider;

	//회원가입
	@PostMapping("/signUp")
	@Operation(summary = "회원가입", description = "이메일과 비밀번호로 회원가입")
	public TodolistResponse<Long> signUp(@Valid @RequestBody SignUpRequest request) {
		User user = new User(request.getEmail(), request.getPassword());
		return TodolistResponse.success(authUseCases.signUp(user));
	}

	//로그인
	@PostMapping("/signIn")
	@Operation(summary = "이메일, 비밀번호로 로그인", description = "이메일과 비밀번호를 통한 일반적인 로그인")
	public TodolistResponse<Void> signIn(@RequestBody SignInRequest request, HttpServletResponse response) {
		User user = authUseCases.signIn(request.getEmail(), request.getPassword());
		response.setHeader(JwtProvider.ACCESS_TOKEN_HEADER_NAME, tokenProvider.generateAccessToken(user));
		response.setHeader(JwtProvider.REFRESH_TOKEN_HEADER_NAME, tokenProvider.generateRefreshToken(user));
		return TodolistResponse.success();
	}

	//토큰 리프레쉬
	@PostMapping("/refresh")
	@Operation(summary = "토큰 재발급", description = "리프레시 토큰을 이용해 엑세스 토큰 재발급")
	public TodolistResponse<Void> refresh(
		@RequestHeader(JwtProvider.ACCESS_TOKEN_HEADER_NAME) String accessToken,
		@RequestHeader(JwtProvider.REFRESH_TOKEN_HEADER_NAME) String refreshToken
		, HttpServletResponse response) {
		tokenProvider.validateRefreshToken(refreshToken);
		UserAuthenticationToken authentication = (UserAuthenticationToken) tokenProvider.getAuthentication(refreshToken);
		User user = authentication.getUser().getDetail();
		response.setHeader(JwtProvider.ACCESS_TOKEN_HEADER_NAME, tokenProvider.generateAccessToken(user));
		response.setHeader(JwtProvider.ACCESS_TOKEN_HEADER_NAME, tokenProvider.generateRefreshToken(user));
		return TodolistResponse.success();
	}

	//로그아웃
	@PostMapping("/logout")
	@Operation(summary = "로그아웃", description = "로그아웃")
	public TodolistResponse<Void> logout(
		 @RequestHeader(JwtProvider.REFRESH_TOKEN_HEADER_NAME) String refreshToken
		, HttpServletResponse response) {
		User user = SecurityUtils.getUser();

		// 엑세스 토큰 만료 처리
		response.setHeader(JwtProvider.ACCESS_TOKEN_HEADER_NAME, tokenProvider.generateAccessToken(user, 0));

		// 리프레시토큰 블랙리스트 처리
		authUseCases.revokeRefreshToken(refreshToken);
		return TodolistResponse.success();
	}

}
