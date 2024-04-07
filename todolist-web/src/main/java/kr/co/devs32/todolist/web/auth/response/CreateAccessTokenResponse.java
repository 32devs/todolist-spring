package kr.co.devs32.todolist.web.auth.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateAccessTokenResponse {
    private String accessToken;
}
