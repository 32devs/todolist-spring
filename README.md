# Todolist - Server side app

이 프로젝트는 투두리스트의 서버 사이드 앱입니다. 
이 애플리케이션은 사용자가 할 일을 관리하고, 할 일을 추가하고, 완료한 할 일을 체크하는 등의 기능을 제공합니다.

## Spec
- Java 21
- Gradle 8.5.1
- Spring Boot 3.2.4
  - Spring Data JPA
  - Spring Security
- Vault

## Features
- [ ] 로그인 기능
- [ ] oauth 기능
- [ ] todolist CRUD

## Architecture
Clean Architecture
![Clean_Architecture.png](doc%2FClean_Architecture.png)

우리는 **빈약한 도메인** 구조를 따르며 각각의 모듈을 통해 레이어를 제한합니다.

### todolist-domain 모듈
- Enterprise Business Rules
- 각 도메인 객체에 대한 정의

### todolist-application
- Application Business Rules
- 실제 비즈니스 요구사항에 관한 구현
- **XXUseCase**: 실제 비즈니스 요구사항 인터페이스
- **XXQuery**: 조회를 위한 인터페이스
- **XXService**: use-case와 query를 구현한 비즈니스 로직

### todolist-adapter
- interface adapters 
- 외부 입력과 출력에 대한 정의
- controller: **web** 통신을 위한 API 인터페이스
- gateways: **DB** 드라이버와 통신을 위한 인터페이스 
 

### Refer
https://velog.io/@devty/Clean-Architecture
https://engineering.linecorp.com/ko/blog/port-and-adapter-architecture
https://www.baeldung.com/spring-boot-clean-architecture
https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html


