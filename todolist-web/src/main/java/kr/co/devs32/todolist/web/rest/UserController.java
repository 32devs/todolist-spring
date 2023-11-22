package kr.co.devs32.todolist.web.rest;

import jakarta.validation.Valid;
import kr.co.devs32.todolist.biz.service.auth.TokenProvider;
import kr.co.devs32.todolist.biz.service.auth.UserService;
import kr.co.devs32.todolist.common.dto.auth.UserDTO;
import kr.co.devs32.todolist.common.request.auth.AddUserRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final long accessTokenValidityInMilliseconds;

    public UserController(UserService userService, TokenProvider tokenProvider
            ,@Value("${jwt.accessToken-validity-in-seconds}") long accessTokenValidityInSeconds) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.accessTokenValidityInMilliseconds = accessTokenValidityInSeconds * 1000;
    }

    //회원가입
    @PostMapping("/api/signup")
    public ResponseEntity<Long> signup(@Valid @RequestBody AddUserRequest addUserRequest){
        return ResponseEntity.ok(userService.save(addUserRequest));
    }

    //로그인
    @PostMapping("/api/login")
    public String login(@RequestBody UserDTO user){
        UserDTO userInfo = userService.findByEmail(user.getEmail());
        return tokenProvider.generateToken(userInfo, accessTokenValidityInMilliseconds);
    }
}
