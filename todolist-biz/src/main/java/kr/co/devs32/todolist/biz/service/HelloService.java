package kr.co.devs32.todolist.biz.service;

import org.springframework.stereotype.Service;

import kr.co.devs32.todolist.dal.repository.HelloRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HelloService {

	private final HelloRepository helloRepository;

	public String hello() {
		return "Hello, Devs32";
	}
}
