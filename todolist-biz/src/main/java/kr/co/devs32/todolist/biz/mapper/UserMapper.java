package kr.co.devs32.todolist.biz.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import kr.co.devs32.todolist.common.dto.auth.UserDTO;
import kr.co.devs32.todolist.dal.entity.auth.UserEntity;

@Mapper
public interface UserMapper {
	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

	UserDTO convert(UserEntity userEntity);

	UserEntity convert(UserDTO userDTO);
}
