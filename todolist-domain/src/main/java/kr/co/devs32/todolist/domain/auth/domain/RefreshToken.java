package kr.co.devs32.todolist.domain.auth.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
public class RefreshToken {
    @Setter
    private Long id;
    private Long userId;
    private String refreshToken;

    public RefreshToken(Long userId, String refreshToken) {
        this.id = null;
        this.userId = userId;
        this.refreshToken = refreshToken;
    }
}
