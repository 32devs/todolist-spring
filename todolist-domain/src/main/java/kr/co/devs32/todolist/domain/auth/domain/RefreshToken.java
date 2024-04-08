package kr.co.devs32.todolist.domain.auth.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
public class RefreshToken {
    @Setter
    private Long id;
    private Long userId;
    private String token;

    public RefreshToken(Long userId, String token) {
        this.id = null;
        this.userId = userId;
        this.token = token;
    }
}
