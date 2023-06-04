package kr.co.devs32.todolist.web.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {
    private String access_token;
//    private String expires_in;
//    private String token_type;
}
