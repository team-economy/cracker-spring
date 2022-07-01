package com.cracker.user.config;

import com.cracker.user.config.jwt.JwtAuthenticationFilter;
import com.cracker.user.config.jwt.JwtAuthorizationFilter;
import com.cracker.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;


// https://github.com/spring-projects/spring-security/issues/10822 참고
@Configuration
@EnableWebSecurity // 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
public class SecurityConfig {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CorsConfig corsConfig;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // csrf 토큰 검사 비활성화
                .csrf().disable()
                // http session 사용 비활성화
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // form을 통한 로그인 비활성화
                .formLogin().disable()
                // http Basic 방식 사용 비활성화
                .httpBasic().disable()
                // 커스텀 필터 적용
                .apply(new MyCustomDsl())
                .and()
                // 인증 범위 설정
                .authorizeRequests(authorize -> authorize
                        // page를 user가 볼 수 있을 때 access 가능한 Role 범위
                        .antMatchers("/**")
                        .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                        // page를 admin이 볼 수 있을 때 access 가능한 Role 범위
                        .antMatchers("/admin/**")
                        .access("hasRole('ROLE_ADMIN')")
                        // 이외는 모두 허가
                        .antMatchers("/login").permitAll()
                        .antMatchers("/signup").permitAll()
                )
                .build();
    }

// /api/v1/login /api/v1/signup
    // Custom Filter
    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http
                    .addFilter(corsConfig.corsFilter())
                    .addFilter(new JwtAuthenticationFilter(authenticationManager))
                    .addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository));
        }
    }
}