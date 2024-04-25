package kr.co.devs32.todolist.dal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import kr.co.devs32.todolist.dal.jpa.refreshtoken.RefreshTokenJpaEntity;
import kr.co.devs32.todolist.domain.auth.domain.RefreshToken;

@Mapper
public interface RefreshTokenMapper {
	RefreshTokenMapper INSTANCE = Mappers.getMapper(RefreshTokenMapper.class);

	RefreshToken convert(RefreshTokenJpaEntity entity);

	RefreshTokenJpaEntity convert(RefreshToken domain);
}
