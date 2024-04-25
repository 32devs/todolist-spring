package kr.co.devs32.todolist.web.legacy.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import kr.co.devs32.todolist.web.legacy.dto.OauthResponseDto;
import kr.co.devs32.todolist.web.legacy.service.OAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("naver")
public class OauthController {

    private final OAuthService oAuthService;

    @GetMapping("/callback")
    public ResponseEntity<OauthResponseDto> callback(@Valid @RequestParam String code ,  @RequestParam String state) {
        log.info("code : " + code + " &&&  state : " + state);
        return ResponseEntity.ok(oAuthService.signUp(code));
    }

}
