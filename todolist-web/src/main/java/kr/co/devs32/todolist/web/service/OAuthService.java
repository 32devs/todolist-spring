package kr.co.devs32.todolist.web.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.devs32.todolist.biz.mapper.UserMapper;
import kr.co.devs32.todolist.biz.service.auth.UserService;
import kr.co.devs32.todolist.common.dto.auth.UserDTO;
import kr.co.devs32.todolist.common.request.auth.AddUserRequest;
import kr.co.devs32.todolist.dal.entity.auth.UserEntity;
import kr.co.devs32.todolist.biz.service.auth.TokenProvider;
import kr.co.devs32.todolist.web.dto.OauthResponseDto;
import kr.co.devs32.todolist.common.response.auth.TokenResponse;
import kr.co.devs32.todolist.biz.service.auth.UserDetailService;
import kr.co.devs32.todolist.biz.service.auth.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService {

    private final UserDetailService userDetailService;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    private final TokenService tokenService;

    private final UserService userService;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String NAVER_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String NAVER_CLEINT_SECRET;

    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String NAVER_REDIRECT_URI;

    @Value("${spring.security.oauth2.client.provider.naver.authorization-uri}")
    private String NAVER_AUTHORIZATION_URI;

    @Value("${spring.security.oauth2.client.provider.naver.token-uri}")
    private String NAVER_TOKEN_URI;

    @Value("${spring.security.oauth2.client.provider.naver.user-info-uri}")
    private String NAVER_USER_INFO_URI;

    private  static final String STATE = "todoList";

    private String CLIENT_ID;
    private String REDIRECT_URI;

    public String getNaverLogin() throws UnsupportedEncodingException {
        return NAVER_AUTHORIZATION_URI+"?response_type=code&client_id="+NAVER_CLIENT_ID+"&redirect_uri="+NAVER_REDIRECT_URI+"&state="+ URLEncoder.encode(STATE, "UTF-8");
    }

    public OauthResponseDto signUp(String code) {

        CLIENT_ID = NAVER_CLIENT_ID;
        REDIRECT_URI = NAVER_REDIRECT_URI;

        //"인가 코드"로 "accessToken" 요청
        String naverAcessToken = getAccessToken(code);

        //토큰으로 네이버 API 호출(이메일 정보 가져오기)
        String email = getUserInfo(naverAcessToken);

        //DB정보 확인 -> 없음면 DB저장
        UserEntity user = registerUserIfNeed(email);

        //JWT 토큰 리턴 & 로그인 처리
        TokenResponse jwtToken = userAuthorizationInput(user);

        //회원여부 닉네임으로 확인
//        Boolean isMember = checkIsMember(user);

        return OauthResponseDto.builder().id(user.getId()).accessToken(jwtToken.getAccessToken()).refreshToken(jwtToken.getRefreshToken()).build();
    }

    private String getAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", CLIENT_ID);
        body.add("client_secret", NAVER_CLEINT_SECRET);
        body.add("redirect_uri", REDIRECT_URI);
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        RestTemplate rt =new RestTemplate();
        ResponseEntity<String> response;

        response = rt.exchange(
                NAVER_TOKEN_URI,
                HttpMethod.POST,
                request,
                String.class
        );

        // HTTP 응답(JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            System.out.println(jsonNode);
            return jsonNode.get("access_token").asText();
        } catch (Exception e) {
            System.out.println("in exception");
            return e.toString();
        }
    }

    private String getUserInfo(String accessToken) {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기 - POST
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response;

        response = rt.exchange(
                NAVER_USER_INFO_URI,
                HttpMethod.POST,
                request,
                String.class
        );

        // reponseBody 정보 꺼내기
        String responseBody = response.getBody();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            System.out.println(jsonNode.get("response"));
            return jsonNode.get("response").get("email").asText();
        } catch (Exception e) {
            return e.toString();
        }
    }

    //DB정보 확인 -> 없으면 DB에 저장
    private UserEntity registerUserIfNeed(String email) {
        // DB에 중복된 이메일 있는지 확인
        Optional<UserDTO> user = Optional.ofNullable(userService.findByEmail(email));

        if(user.isEmpty()){
            //DB에 정보 등록
            UserEntity newUser = UserEntity.builder()
                    .email(email)
                    .password(passwordEncoder.encode("naver"))
//                    .type("naver")
                    .build();
            AddUserRequest dto = new AddUserRequest();
            dto.setEmail(newUser.getEmail());
            dto.setPassword(newUser.getPassword());
            userService.save(dto);
        }
        return UserMapper.INSTANCE.convert(userService.findByEmail(email));
    }

    private TokenResponse userAuthorizationInput(UserEntity user) {
        UserDetails userDetails = userDetailService.loadUserByUsername(user.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                "",
                userDetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenResponse tokenResponse = tokenService.createAllToken(UserMapper.INSTANCE.convert(user));
        tokenProvider.saveRefreshToken(user.getId(), tokenResponse.getRefreshToken());

        return tokenResponse;
    }

//    private Boolean checkIsMember(User user) {
//        return user.getNickname() != null;
//    }

}
