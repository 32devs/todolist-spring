package kr.co.devs32.todolist.web.rest;

import jakarta.validation.Valid;
import kr.co.devs32.todolist.web.config.jwt.TokenProvider;
import kr.co.devs32.todolist.web.dto.AddUserRequest;
import kr.co.devs32.todolist.web.entity.User;
import kr.co.devs32.todolist.web.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    private final TokenProvider tokenProvider;

    //회원가입
    @PostMapping("/api/signup")
    public ResponseEntity<Long> signup(@Valid @RequestBody AddUserRequest addUserRequest){
        return ResponseEntity.ok(userService.save(addUserRequest));
    }

    //로그인
    @PostMapping("/api/login")
    public String login(@RequestBody User user){
        User userInfo = userService.findByEmail(user.getEmail());
        return tokenProvider.generateToken(userInfo, Duration.ofHours(2));
    }
}
