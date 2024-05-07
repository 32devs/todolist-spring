package kr.co.devs32.todolist.domain.auth.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.co.devs32.todolist.domain.auth.domain.User;
import kr.co.devs32.todolist.domain.auth.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
    void testPersist() {
        User user = new User("user@gmail.com", "password");
        when(userRepository.persist(user)).thenReturn(user);
        User savedUser = userService.persist(user);
        assertEquals(savedUser, user);
    }

    @Test
    void testFindByEmailWhenUserExists() {
        User user = new User("user@gmail.com", "password");
        when(userRepository.findByEmail("user@gmail.com")).thenReturn(Optional.of(user));
        Optional<User> fetchedUser = userService.findByEmail("user@gmail.com");
        assertThat(fetchedUser)
            .isPresent()
            .contains(user);
    }

    @Test
    void testFindByEmailWhenUserNotExists() {
        when(userRepository.findByEmail("user_no@gmail.com")).thenReturn(Optional.empty());
        Optional<User> fetchedUser = userService.findByEmail("user_no@gmail.com");
        assertThat(fetchedUser).isEmpty();
    }
    @Test
    void testGetWhenUserExists() {
        User user = new User("user@gmail.com", "password");
        when(userRepository.get(user.getId())).thenReturn(Optional.of(user));
        User fetchedUser = userService.get(user.getId());
        assertThat(fetchedUser).isEqualTo(user);
    }

}
