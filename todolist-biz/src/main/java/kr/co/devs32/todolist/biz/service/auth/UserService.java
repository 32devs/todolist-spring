package kr.co.devs32.todolist.biz.service.auth;

import kr.co.devs32.todolist.biz.mapper.UserMapper;
import kr.co.devs32.todolist.common.dto.auth.UserDTO;
import kr.co.devs32.todolist.common.request.auth.AddUserRequest;
import kr.co.devs32.todolist.dal.entity.auth.UserEntity;
import kr.co.devs32.todolist.dal.repository.auth.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserEntityRepository userEntityRepository;

    //회원가입
    public Long save(AddUserRequest dto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return userEntityRepository.save(UserEntity.builder()
                .email(dto.getEmail())
                //패스워드 암호화
                .password(encoder.encode(dto.getPassword()))
                .build()).getId();
    }

    public UserDTO findById(Long userId) {
        return userEntityRepository.findById(userId)
            .map(UserMapper.INSTANCE::convert)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    public UserDTO findByEmail(String email) {
        return userEntityRepository.findByEmail(email)
            .map(UserMapper.INSTANCE::convert)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }
}
