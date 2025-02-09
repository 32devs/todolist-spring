package kr.co.devs32.todolist.dal.jpa.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"kr.co.devs32.todolist.dal"})
@EntityScan(basePackages = {"kr.co.devs32.todolist.dal"})
public class JpaConfig {
}
