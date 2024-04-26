package kr.co.devs32.todolist.domain.todo.repository;

import java.util.List;

import kr.co.devs32.todolist.domain.todo.domain.Todo;
import kr.co.devs32.todolist.domain.todo.domain.TodoSearchQuery;

public interface TodoRepository {

	// 저장
	Todo persist(Todo todo);

	// 검색
	List<Todo> search(TodoSearchQuery query);

}
