package kr.co.devs32.todolist.biz.service.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import kr.co.devs32.todolist.common.dto.auth.UserDTO;
import kr.co.devs32.todolist.common.response.auth.TokenResponse;

@Service
public class TokenService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    private final long accessTokenValidityInMilliseconds;
    private final long refreshTokenValidityInMilliseconds;

    public TokenService(TokenProvider tokenProvider, RefreshTokenService refreshTokenService, UserService userService
            ,@Value("${jwt.accessToken-validity-in-seconds}") long accessTokenValidityInSeconds
            ,@Value("${jwt.refreshToken-validity-in-seconds}") long refreshTokenValidityInSeconds) {
        this.tokenProvider = tokenProvider;
        this.refreshTokenService = refreshTokenService;
        this.userService = userService;
        this.accessTokenValidityInMilliseconds = accessTokenValidityInSeconds * 1000;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInSeconds * 1000;
    }

    public String createNewAccessToken(String refreshToken) {
        //토큰 유효성 검사에 실패하면 예외 발생
        if(!tokenProvider.vaildToken(refreshToken)){
            throw new IllegalArgumentException("Unexpected token");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        UserDTO user = userService.findById(userId);

        return tokenProvider.generateToken(user, accessTokenValidityInMilliseconds);
    }

    public TokenResponse createAllToken(UserDTO userInfo) {
        //accessToken, refreshToken 생성
        String accessToken = tokenProvider.generateToken(userInfo, accessTokenValidityInMilliseconds); //1시간
        String refreshToken = tokenProvider.generateToken(userInfo, refreshTokenValidityInMilliseconds); //7일

        return new TokenResponse(accessToken, refreshToken);
    }

}
