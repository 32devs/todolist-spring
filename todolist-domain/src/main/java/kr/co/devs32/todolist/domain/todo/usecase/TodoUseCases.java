package kr.co.devs32.todolist.domain.todo.usecase;

import java.util.List;

import kr.co.devs32.todolist.domain.todo.domain.Todo;
import kr.co.devs32.todolist.domain.todo.domain.TodoSearchQuery;

public interface TodoUseCases {

	// 할일 저장
	Todo persist(Todo todo);

	// 할일 조회
	List<Todo> search(TodoSearchQuery query);

	// 할일 수정
	Todo update(Todo todo);

	// 할일 삭제
	Todo remove(Todo todo);

	// 할일 완료
	Todo complete(Todo todo);

}
