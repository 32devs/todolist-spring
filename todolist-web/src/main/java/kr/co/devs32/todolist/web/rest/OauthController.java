package kr.co.devs32.todolist.web.rest;

import kr.co.devs32.todolist.web.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.UnsupportedEncodingException;


@Controller
@RequiredArgsConstructor
public class OauthController {

    private final LoginService loginService;

    //네이버 인증 서버로부터 인증 코드를 내려받고 인증 코드를 이용해 액세스 토큰을 요청합니다. 그리고 네이버 인증 서버로부터 내려받은 액세스 토큰으로 사용자 정보를 가져옵니다.
    @GetMapping("/")
    public String Login(Model model) throws UnsupportedEncodingException {
        model.addAttribute("naverUrl", loginService.getNaverLogin());
        return "index";
    }
}
