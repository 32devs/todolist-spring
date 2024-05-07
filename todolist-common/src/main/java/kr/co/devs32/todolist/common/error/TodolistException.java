package kr.co.devs32.todolist.common.error;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TodolistException extends RuntimeException {
	protected final BaseErrorCode errorCode;
	protected final String code;
	protected final HttpStatus httpStatus;
	protected final String message;
	protected final Throwable cause;

	@Builder
	public TodolistException(BaseErrorCode errorCode, HttpStatus httpStatus,
		String message, Throwable cause) {
		if (errorCode == null) {
			throw new IllegalArgumentException("errorCode is a required field and must not be null.");
		}
		this.code = errorCode.getDomain() + "_" + errorCode.name();
		this.errorCode = errorCode;
		this.httpStatus = (httpStatus == null) ? HttpStatus.INTERNAL_SERVER_ERROR : httpStatus;
		this.message = (message == null) ? errorCode.getMessage() : message;
		this.cause = cause;
	}
}
