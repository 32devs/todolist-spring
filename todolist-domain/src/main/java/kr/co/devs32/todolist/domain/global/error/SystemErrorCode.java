package kr.co.devs32.todolist.domain.global.error;

import kr.co.devs32.todolist.common.error.BaseErrorCode;
import lombok.Getter;

@Getter
public enum SystemErrorCode implements BaseErrorCode {
	UNKNOWN_ERROR("오류를 확인해주세요"),
	;

	final String domain = "SYSTEM";
	final String message;

	SystemErrorCode(String message) {
		this.message = message;
	}
}
