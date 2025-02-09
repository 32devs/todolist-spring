package kr.co.devs32.todolist.dal.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import kr.co.devs32.todolist.dal.jpa.auth.UserJpaEntity;
import kr.co.devs32.todolist.domain.auth.domain.User;

@Mapper
public interface UserJapEntityMapper {
	UserJapEntityMapper INSTANCE = Mappers.getMapper(UserJapEntityMapper.class);

	User convert(UserJpaEntity entity);

	UserJpaEntity convert(User domain);
}
