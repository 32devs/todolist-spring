package kr.co.devs32.todolist.dal.jpa.refreshtoken;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "refresh_token")
public class RefreshTokenJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

     @Column(name = "refresh_token", nullable = false)
    private String refreshToken;
}
