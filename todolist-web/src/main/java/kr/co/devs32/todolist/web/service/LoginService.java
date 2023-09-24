package kr.co.devs32.todolist.web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Service
@RequiredArgsConstructor
public class LoginService {

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String NAVER_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String NAVER_CLEINT_SECRET;

    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String NAVER_REDIRECT_URI;

    @Value("${spring.security.oauth2.client.provider.naver.authorization-uri}")
    private String NAVER_AUTHORIZATION_URI;

    private final static String state = "todoList";

    public String getNaverLogin() throws UnsupportedEncodingException {
        return NAVER_AUTHORIZATION_URI+"?response_type=code&client_id="+NAVER_CLIENT_ID+"&redirect_uri="+NAVER_REDIRECT_URI+"&state="+ URLEncoder.encode(state, "UTF-8");
    }
}
