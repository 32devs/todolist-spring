package kr.co.devs32.todolist.dal.jpa.auth;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import kr.co.devs32.todolist.dal.jpa.mapper.UserJapEntityMapper;
import kr.co.devs32.todolist.domain.auth.domain.User;
import kr.co.devs32.todolist.domain.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserJpaRepository implements UserRepository {

    private final UserJpaEntityRepository jpaEntityRepository;

    @Override
    public User persist(User user) {
        UserJpaEntity entity = UserJapEntityMapper.INSTANCE.convert(user);
        jpaEntityRepository.save(entity);
        user.setId(entity.getId());
        return user;
    }

    @Override
    public Optional<User> get(Long id) {
        Optional<UserJpaEntity> optional = jpaEntityRepository.findById(id);
		return optional.map(UserJapEntityMapper.INSTANCE::convert);
	}

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<UserJpaEntity> optional = jpaEntityRepository.findByEmail(email);
        return optional.map(UserJapEntityMapper.INSTANCE::convert);
    }
}
