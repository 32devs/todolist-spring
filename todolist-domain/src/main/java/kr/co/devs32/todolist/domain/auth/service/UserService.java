package kr.co.devs32.todolist.domain.auth.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import kr.co.devs32.todolist.domain.auth.domain.User;
import kr.co.devs32.todolist.domain.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService implements UserUseCases {

    private final UserRepository userRepository;

    @Override
    public User persist(User user) {
        return userRepository.persist(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User get(Long id) {
        return userRepository.get(id)
            .orElseThrow(() -> new NoSuchElementException("not found user"));
    }
}
