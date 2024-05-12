package kr.co.devs32.todolist.web.global.handler;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import kr.co.devs32.todolist.common.error.TodolistException;
import kr.co.devs32.todolist.web.global.response.TodolistResponse;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(TodolistException.class)
	public TodolistResponse<Void> handleTodolistException(TodolistException e) {
		log.error(e.getMessage(), e);
		return TodolistResponse.error(e);
	}

	@ExceptionHandler(RuntimeException.class)
	public TodolistResponse<Void> handleTodolistException(RuntimeException e) {
		log.error(e.getMessage(), e);
		return TodolistResponse.error(e);
	}
}
