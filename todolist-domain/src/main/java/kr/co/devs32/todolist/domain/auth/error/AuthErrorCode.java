package kr.co.devs32.todolist.domain.auth.error;

import kr.co.devs32.todolist.common.error.BaseErrorCode;
import lombok.Getter;

@Getter
public enum AuthErrorCode implements BaseErrorCode {
	INVALID_LOGIN("아이디/패스워드를 확인해주세요"),
	DUPLICATED_USER("이미 유저가 존재합니다."),
	NOT_FOUND_USER("유저를 찾을 수 없습니다."),
	;

	final String domain = "AUTH";
	final String message;

	AuthErrorCode(String message) {
		this.message = message;
	}
}
