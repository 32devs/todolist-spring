package kr.co.devs32.todolist.web.legacy.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import kr.co.devs32.todolist.biz.service.auth.*;
import kr.co.devs32.todolist.common.dto.auth.UserDTO;
import kr.co.devs32.todolist.web.legacy.request.CreateAccessTokenRequest;
import kr.co.devs32.todolist.web.legacy.response.CreateAccessTokenResponse;
import kr.co.devs32.todolist.web.legacy.response.TokenResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class TokenApiController {
    private final TokenService tokenService;

    private final UserService userService;

    private final TokenProvider tokenProvider;

    @PostMapping("/api/token")
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken
            (@RequestBody CreateAccessTokenRequest request) {
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateAccessTokenResponse(newAccessToken));
    }

    @PostMapping("/api/authenticate")
    public ResponseEntity<TokenResponse> createAllToken (@RequestBody UserDTO user, HttpServletResponse response) {
        //유저 정보 확인
        UserDTO userInfo = userService.findByEmail(user.getEmail());
        //토큰 발급(access, refresh)
        TokenResponse tokenResponse = tokenService.createAllToken(userInfo);
        //리프레쉬 토큰 DB저장
        tokenProvider.saveRefreshToken(userInfo.getId(), tokenResponse.getRefreshToken());
        //토큰 헤더 설정
        tokenProvider.setHeaderAccessToken(response, tokenResponse.getAccessToken());
        tokenProvider.setHeaderRefreshToken(response, tokenResponse.getRefreshToken());
        return ResponseEntity.ok(tokenResponse);
    }
}
