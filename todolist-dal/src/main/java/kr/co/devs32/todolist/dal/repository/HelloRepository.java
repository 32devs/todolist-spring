package kr.co.devs32.todolist.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.devs32.todolist.dal.entity.HelloEntity;

public interface HelloRepository extends JpaRepository<HelloEntity, Integer> {
}
