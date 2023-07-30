package kr.co.devs32.todolist.web.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"kr.co.devs32.todolist.web.repository"})
@EntityScan(basePackages = {"kr.co.devs32.todolist.web.entity"})
public class JpaWebConfig {
}
