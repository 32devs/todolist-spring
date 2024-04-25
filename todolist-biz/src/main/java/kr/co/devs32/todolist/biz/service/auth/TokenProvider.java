package kr.co.devs32.todolist.biz.service.auth;

import java.util.*;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.devs32.todolist.common.dto.auth.UserDTO;
import kr.co.devs32.todolist.dal.entity.auth.RefreshTokenEntity;
import kr.co.devs32.todolist.dal.repository.auth.RefreshTokenEntityRepository;
import kr.co.devs32.todolist.web.security.token.jwt.JwtProperties;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TokenProvider {
    private final JwtProperties jwtProperties;

    private final RefreshTokenEntityRepository refreshTokenEntityRepository;

    public String generateToken(UserDTO user, long expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt), user);
    }

    //JWT 토큰 생성 메서드
    private String makeToken(Date expiry, UserDTO user) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // 헤더 typ : JWT
                //내용 iss : pjhwin1@gmail.com
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)   // 현재시간
                .setExpiration(expiry)  //expiry 멤버 변숫값
                .setSubject(user.getEmail()) //유저 이메일
                .claim("id", user.getId()) //유저 아이디
                //서명 : 비밀값과 함께 해시값을 HS256 방식으로 암호화
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    //JWT 토큰 유효성 검증 메서드
    public boolean vaildToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey()) //비밀값으로 복호화
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //JWT refresh 토큰 유효성 검증
    public boolean refreshTokenVaildToken(String token) {
        // 1차 토큰 검증
        if(!vaildToken(token)) return false;

        // DB에 저장한 토큰 비교
        Optional<RefreshTokenEntity> refreshToken = refreshTokenEntityRepository.findByRefreshToken(token);

        return refreshToken.isPresent() && token.equals(refreshToken.get().getRefreshToken());
    }

    //토큰 기반으로 인증 정보를 가져오는 메서드
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(new org.springframework.
                security.core.userdetails.User(claims.getSubject(),"",authorities), token, authorities);
    }

    //토큰 기반으로 유저 ID를 가져오는 메서드
    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()    //클레임 조회
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }

    //accessToken Header 설정
    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader("Access_Token", accessToken);
    }

    //refreshToken Header 설정
    public void setHeaderRefreshToken(HttpServletResponse response, String refreshToken ) {
        response.setHeader("Refresh_Token", refreshToken);
    }

    //refreshToken DB저장
    public void saveRefreshToken(Long userId, String newRefreshToken){
        RefreshTokenEntity refreshTokenEntity = refreshTokenEntityRepository.findByUserId(userId)
                .map(entity -> entity.update(newRefreshToken))
                .orElse(new RefreshTokenEntity(userId, newRefreshToken));

        refreshTokenEntityRepository.save(refreshTokenEntity);
    }

}
