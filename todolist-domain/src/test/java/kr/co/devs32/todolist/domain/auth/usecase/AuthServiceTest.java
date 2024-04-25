package kr.co.devs32.todolist.domain.auth.usecase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.co.devs32.todolist.domain.auth.domain.RefreshToken;
import kr.co.devs32.todolist.domain.auth.domain.User;
import kr.co.devs32.todolist.domain.auth.repository.RefreshTokenRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    private static final String TEST_EMAIL = "testuser@mail.com";
    private static final String TEST_PASSWORD = "testpassword";
    private static final String VALID_EMAIL = "test@email.com";
    private static final String NON_EXISTENT_EMAIL = "nonexistent@email.com";
    private static final String VALID_PASSWORD = "password";
    private static final String WRONG_PASSWORD = "wrong_password";

    @Mock
    private UserUseCases userUseCases;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    void givenNewUser_whenSignUp_thenUserIdIsReturned() {
        // Given
        User newUser = new User(TEST_EMAIL, TEST_PASSWORD);
        given(userUseCases.findByEmail(any())).willReturn(Optional.empty());
        given(userUseCases.persist(any())).willReturn(newUser);

        // When
        Long userId = authService.signUp(newUser);

        // Then
        then(userUseCases).should().persist(any());
        assertEquals(newUser.getId(), userId);
    }

    @Test
    void givenExistingUser_whenSignUp_thenExceptionIsThrown() {
        // Given
        User existingUser = new User(TEST_EMAIL, TEST_PASSWORD);
        given(userUseCases.findByEmail(any())).willReturn(Optional.of(existingUser));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> authService.signUp(existingUser));
    }

    @Test
    void signIn_withValidCredentials_returnsUser() {
        // Given
        User user = new User(VALID_EMAIL, VALID_PASSWORD);

        given(userUseCases.findByEmail(VALID_EMAIL)).willReturn(Optional.of(user));
        given(user.isMatchPassword(VALID_PASSWORD)).willReturn(true);

        // When
        User signedUser = authService.signIn(VALID_EMAIL, VALID_PASSWORD);

        // Then
        assertNotNull(signedUser);
        assertEquals(VALID_EMAIL, signedUser.getEmail());
    }

    @Test
    void signIn_withNonExistentEmail_throwsException() {
        // Given
        given(userUseCases.findByEmail(NON_EXISTENT_EMAIL)).willReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> authService.signIn(NON_EXISTENT_EMAIL, VALID_PASSWORD));
    }

    @Test
    void signIn_withIncorrectPassword_throwsException() {
        // Given
        User user = new User(VALID_EMAIL, VALID_PASSWORD);

        given(userUseCases.findByEmail(VALID_EMAIL)).willReturn(Optional.of(user));
        given(user.isMatchPassword(WRONG_PASSWORD)).willReturn(false);

        // When & Then
        assertThrows(IllegalStateException.class, () -> authService.signIn(VALID_EMAIL, WRONG_PASSWORD));
    }

    @Test
    void testFindByRefreshToken() {
        // given
        String testToken = "test_token";
        RefreshToken expectedToken = new RefreshToken(1L, testToken);

        given(refreshTokenRepository.findByToken(testToken)).willReturn(Optional.of(expectedToken));

        // when
        Optional<RefreshToken> resultToken = authService.findByRefreshToken(testToken);

        // then
        assertThat(resultToken, is(Optional.of(expectedToken)));
    }
}
