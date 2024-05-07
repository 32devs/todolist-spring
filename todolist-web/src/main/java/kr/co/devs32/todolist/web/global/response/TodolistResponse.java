package kr.co.devs32.todolist.web.global.response;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

import kr.co.devs32.todolist.common.error.TodolistException;
import kr.co.devs32.todolist.domain.global.error.SystemErrorCode;
import lombok.Getter;

@Getter
public class TodolistResponse<T> {
	private final int status;
	private final boolean success;
	private final T data;
	private final String message;
	private final String errorCode;

	public TodolistResponse(HttpStatus status, boolean success, T data, TodolistException exception, String message) {
		this.status = status.value();
		this.success = success;
		this.data = data;
		this.errorCode = exception != null ? exception.getCode() : null;
		this.message = message;
	}

	public static TodolistResponse<Void> success() {
		return success(null);
	}

	public static <T> TodolistResponse<T> success(@Nullable T data) {
		return new TodolistResponse<>(HttpStatus.OK, true, data, null, null);
	}

	public static <T> TodolistResponse<T> fail(String message) {
		return new TodolistResponse<>(HttpStatus.OK, false, null, null, message);
	}

	public static <T> TodolistResponse<T> error(TodolistException exception) {
		return new TodolistResponse<>(exception.getHttpStatus(), false, null, exception, exception.getMessage());
	}

	public static <T> TodolistResponse<T> error(RuntimeException runtimeException) {
		TodolistException exception = TodolistException.builder()
			.errorCode(SystemErrorCode.UNKNOWN_ERROR)
			.httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
			.cause(runtimeException)
			.message(runtimeException.getLocalizedMessage())
			.build();
		return error(exception);
	}
}
