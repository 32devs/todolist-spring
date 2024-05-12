package kr.co.devs32.todolist.common.error;


public interface BaseErrorCode {
	String name();
	String getDomain();
	String getMessage();
}
