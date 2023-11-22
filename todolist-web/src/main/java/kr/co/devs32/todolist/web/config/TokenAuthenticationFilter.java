package kr.co.devs32.todolist.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.devs32.todolist.biz.service.auth.TokenProvider;
import kr.co.devs32.todolist.biz.service.auth.TokenService;
import kr.co.devs32.todolist.common.dto.GlobalResDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    private final TokenService tokenService;
//    private final static String HEADER_AUTHORIZATION = "Authorization";
//    private final static String TOKEN_PREFIX = "Bearer ";

    public static final String ACCESS_TOKEN = "Access_Token";
    public static final String REFRESH_TOKEN = "Refresh_Token";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 요청 헤더의 Authorization 키의 값 조회
//        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
        // 가져온 값에서 접두사 제거
//        String token = getAccessToken(authorizationHeader);

        // Access / Refresh 헤더에서 토큰 가져오기
        String accessToken = getHeaderToken(request, "Access");
        String refreshToken = getHeaderToken(request, "Refresh");

        // 가져온 access 토큰이 있으면
        if(accessToken != null){
            //토큰이 유효한지 확인하고, 유효한 때는 인증 정보 설정
            if(tokenProvider.vaildToken(accessToken)){
                Authentication authentication = tokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            //access 토큰이 만료됨 && refresh 토큰이 있음
            else if(refreshToken != null){
                // refresh 토큰 검증 && refresh 토큰 DB에서 존재 유무확인
                boolean isRefreshToken = tokenProvider.refreshTokenVaildToken(refreshToken);
                // refresh 토큰이 유효 && DB와 비교하여 똑같다면
                if(isRefreshToken) {
                    // 새로운 access 토큰 발급
                    String newAccessToken = tokenService.createNewAccessToken(refreshToken);
                    // Header access 토큰 추가
                    tokenProvider.setHeaderAccessToken(response, newAccessToken);
                    //인증 정보 설정
                    Authentication authentication = tokenProvider.getAuthentication(newAccessToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                // refresh 토큰이 만료 || refresh 토큰이 DB와 비교하여 다른 경우
                else {
                    jwtExceptionHandler(response, "RefreshToken Expired", HttpStatus.BAD_REQUEST);
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    // header 토큰을 가져오는 기능
    public String getHeaderToken(HttpServletRequest request, String type) {
        return type.equals("Access") ? request.getHeader(ACCESS_TOKEN) : request.getHeader(REFRESH_TOKEN);
    }

    // Jwt 예외처리
    public void jwtExceptionHandler(HttpServletResponse response, String msg, HttpStatus status){
        response.setStatus(status.value());
        response.setContentType("application/json");
        try {
            String json = new ObjectMapper().writeValueAsString(new GlobalResDTO(msg, status.value()));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

}
