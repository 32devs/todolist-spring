package kr.co.devs32.todolist.web.repository;

import kr.co.devs32.todolist.web.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    /*
     * findOneWithAuthoritiesByUsername
     * username을 기준으로 User 정보 ( authorities 정보 포함 ) 를 가져오는 역할을 수행
     * */
    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByUsername(String username);
}
