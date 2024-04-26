package kr.co.devs32.todolist.web.auth.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {
    private String email;
    private String password;
}
