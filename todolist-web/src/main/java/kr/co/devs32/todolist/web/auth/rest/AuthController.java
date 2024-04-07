package kr.co.devs32.todolist.web.auth.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kr.co.devs32.todolist.common.request.auth.AddUserRequest;
import kr.co.devs32.todolist.domain.auth.domain.User;
import kr.co.devs32.todolist.domain.auth.service.UserAuthUseCases;
import kr.co.devs32.todolist.web.auth.JwtTokenProvider;
import kr.co.devs32.todolist.web.auth.model.UserAuthenticationToken;
import kr.co.devs32.todolist.web.auth.request.SignInRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
	private final UserAuthUseCases userAuthUseCases;
	private final JwtTokenProvider tokenProvider;

	//회원가입
	@PostMapping("/signUp")
	public ResponseEntity<Long> signUp(@Valid @RequestBody AddUserRequest request) {
		User user = new User(request.getEmail(), request.getPassword());
		return ResponseEntity.ok(userAuthUseCases.signUp(user));
	}

	//로그인
	@PostMapping("/signIn")
	public ResponseEntity<Void> signIn(@RequestBody SignInRequest request, HttpServletResponse response) throws
		Exception {
		User user = userAuthUseCases.signIn(request.getEmail(), request.getPassword());
		response.setHeader(JwtTokenProvider.ACCESS_TOKEN_HEADER_NAME, tokenProvider.generateAccessToken(user));
		response.setHeader(JwtTokenProvider.ACCESS_TOKEN_HEADER_NAME, tokenProvider.generateRefreshToken(user));
		return ResponseEntity.ok(null);
	}

	//로그아웃
	@PostMapping("/logout")
	public ResponseEntity<Void> logout(@RequestHeader(JwtTokenProvider.ACCESS_TOKEN_HEADER_NAME) String accessToken
		, @RequestHeader(JwtTokenProvider.REFRESH_TOKEN_HEADER_NAME) String refreshToken
		, HttpServletResponse response) {
		UserAuthenticationToken token = (UserAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
		User user = token.getUser();
		response.setHeader(JwtTokenProvider.ACCESS_TOKEN_HEADER_NAME, tokenProvider.generateAccessToken(user, 0));
		return ResponseEntity.ok(null);
	}

}
