package kr.co.devs32.todolist.web.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties("jwt")
public class JwtProperties {
    private String issuer;
    private String secretKey;
}
