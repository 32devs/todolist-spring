package kr.co.devs32.todolist.domain.auth.repository.jpa;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import kr.co.devs32.todolist.dal.jpa.user.UserJpaEntity;
import kr.co.devs32.todolist.dal.jpa.user.UserJpaEntityRepository;
import kr.co.devs32.todolist.domain.auth.domain.User;
import kr.co.devs32.todolist.domain.auth.repository.UserRepository;
import kr.co.devs32.todolist.domain.mapper.UserJapEntityMapper;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserJpaRepository implements UserRepository {

    private final UserJpaEntityRepository repository;

    @Override
    public User persist(User user) {
        UserJpaEntity entity = UserJapEntityMapper.INSTANCE.convert(user);
        repository.save(entity);
        user.setId(entity.getId());
        return user;
    }

    @Override
    public Optional<User> get(Long id) {
        Optional<UserJpaEntity> optional = repository.findById(id);
		return optional.map(UserJapEntityMapper.INSTANCE::convert);
	}

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<UserJpaEntity> optional = repository.findByEmail(email);
        return optional.map(UserJapEntityMapper.INSTANCE::convert);
    }
}
