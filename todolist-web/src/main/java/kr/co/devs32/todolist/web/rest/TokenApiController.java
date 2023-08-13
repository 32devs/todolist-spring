package kr.co.devs32.todolist.web.rest;

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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Optional;

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
    public ResponseEntity<TokenResponse> Token (@RequestBody User user) {
        //유저 정보 확인
        User userInfo = userService.findByEmail(user.getEmail());
        return ResponseEntity.ok(tokenService.createAllToken(userInfo));
    }
}
