package kr.co.devs32.todolist.web.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import kr.co.devs32.todolist.web.security.filter.JwtAuthenticationFilter;
import kr.co.devs32.todolist.web.security.token.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class WebJwtSecurityConfig {
	private final JwtProvider tokenProvider;

	@Bean
	public WebSecurityCustomizer configure() {
		// 정적 파일 시큐리티 비활성화
		return web -> web.ignoring()
			.requestMatchers("/img/**", "/css/**", "/js/**");
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// 토큰 방식으로 인증을 하기 때문에 폼로그인, 세션 비활성화
		http.csrf(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.logout(AbstractHttpConfigurer::disable)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		// 헤더를 확인할 커스텀 필터 추가
		http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

		// 토큰 재발급 URL은 인증 없이 접근 가능하도록 설정. 나머지 API URL은 인증 필요
		http.authorizeHttpRequests(
			auth -> auth
				.requestMatchers(permittedEndpoints()).permitAll()
				.requestMatchers(authEndpoints()).permitAll()
				.requestMatchers(apiEndpoint()).authenticated()
				.anyRequest().denyAll()
		);

		http.logout(logout -> logout.logoutSuccessUrl("/login"));

		// /api로 시작하는 url인 경우 401 상태 코드를 반환하도록 예외처리
		http.exceptionHandling(
			config -> config.defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
				apiEndpoint()));

		return http.build();

	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter(tokenProvider);
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	private RequestMatcher[] authEndpoints() {
		return new RequestMatcher[] {
			new AntPathRequestMatcher("/api/v1/auth/signIn", HttpMethod.POST.name()), // 로그인
			new AntPathRequestMatcher("/api/v1/auth/signUp", HttpMethod.POST.name()), // 회원가입
			new AntPathRequestMatcher("/api/v1/auth/refresh", HttpMethod.POST.name()), // 토큰 재발급
			new AntPathRequestMatcher("/api/v1/auth/logout", HttpMethod.POST.name()), // 로그아웃
		};
	}

	private RequestMatcher[] permittedEndpoints() {
		return new RequestMatcher[] {
			new AntPathRequestMatcher("/doc/**") // swagger
		};
	}

	private RequestMatcher apiEndpoint() {
		return new AntPathRequestMatcher("/api/**");
	}
}
