package kr.co.devs32.todolist.web.rest;

import jakarta.servlet.http.HttpServletResponse;
import kr.co.devs32.todolist.web.config.jwt.TokenProvider;
import kr.co.devs32.todolist.web.dto.CreateAccessTokenRequest;
import kr.co.devs32.todolist.web.dto.CreateAccessTokenResponse;
import kr.co.devs32.todolist.web.dto.TokenResponse;
import kr.co.devs32.todolist.web.entity.User;
import kr.co.devs32.todolist.web.service.TokenService;
import kr.co.devs32.todolist.web.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<TokenResponse> createAllToken (@RequestBody User user, HttpServletResponse response) {
        //유저 정보 확인
        User userInfo = userService.findByEmail(user.getEmail());
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
