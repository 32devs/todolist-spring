package kr.co.devs32.todolist.biz.service;

import org.springframework.stereotype.Service;

import kr.co.devs32.todolist.common.dto.HelloDTO;
import kr.co.devs32.todolist.dal.entity.HelloEntity;
import kr.co.devs32.todolist.dal.repository.HelloRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HelloService {

	private final HelloRepository helloRepository;

	public String hello() {
		HelloEntity helloEntity = helloRepository.findById(1).orElseThrow(() -> new RuntimeException("DB 확인필요"));
		HelloDTO hello = new HelloDTO(helloEntity.getId(), helloEntity.getName());
		return "hello "+ hello.getName();
	}
}
