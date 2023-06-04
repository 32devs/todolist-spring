package kr.co.devs32.todolist.web.rest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.devs32.todolist.biz.service.HelloService;
import lombok.RequiredArgsConstructor;
@RestController
@RequiredArgsConstructor
public class HelloController {

	private final HelloService helloService;

	@RequestMapping("/hello")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public String hello() {
		return helloService.hello();
	}
}
