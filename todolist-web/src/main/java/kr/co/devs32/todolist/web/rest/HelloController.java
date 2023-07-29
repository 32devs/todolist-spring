package kr.co.devs32.todolist.web.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.devs32.todolist.biz.service.HelloService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class HelloController {

	private final HelloService helloService;

	@RequestMapping("/hello")
	public String hello() {
		return helloService.hello();
	}
}
