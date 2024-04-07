package kr.co.devs32.todolist.domain.auth.domain;

import lombok.Getter;

@Getter
public class RefreshToken {
    private Long id;
    private Long userId;
    private String token;

    public RefreshToken(Long userId, String token) {
        this.id = null;
        this.userId = userId;
        this.token = token;
    }
}
