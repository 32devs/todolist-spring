package kr.co.devs32.todolist.biz.service.auth;

import kr.co.devs32.todolist.dal.entity.auth.UserEntity;
import kr.co.devs32.todolist.dal.repository.auth.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
// 스프링 시큐리티에서 사용자 정보를 가져오는 인터페이스
public class UserDetailService implements UserDetailsService {

    private final UserEntityRepository userEntityRepository;

    // 사용자 이름(email)으로 사용자의 정보를 가져오는 메서드
    @Override
    public UserEntity loadUserByUsername(String email) {
        return userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException((email)));
    }
}
