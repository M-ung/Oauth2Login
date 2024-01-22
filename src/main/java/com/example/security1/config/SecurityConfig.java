package com.example.security1.config;

import com.example.security1.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록된다.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성화. preAuthorize 어노테이션 활성화.
public class SecurityConfig {
    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    // 해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다.
    @Bean
    public BCryptPasswordEncoder encoderPwd() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(CsrfConfigurer::disable) // CSRF 보호 기능 비활성화
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/user/**").authenticated()
                        .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                        .anyRequest().permitAll()
                )
                .formLogin(login -> login
                        .loginPage("/loginForm")
                        .loginProcessingUrl("/login") // login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행해준다.
                        .defaultSuccessUrl("/") // 로그인이 완료되면 일로 이동한다.
                )
                .oauth2Login(login -> login
                                // 구글 로그인이 완료된 뒤의 후처리가 필요하다!! (엑세스 토큰 + 사용자 프로필 정보)
                        .loginPage("/loginForm")
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(principalOauth2UserService))
                        // 1. 코드 받기(인증)
                        // 2. 엑세스 토큰(권한)
                        // 3. 사용자 프로필 정보를 가져오고
                        // 4. 그 정보를 토대로 회원가입을 자동으로 진행시키거나
                        // 5. 또는 정보가 모자르다면 추가적인 회원가입 창을 띄워 추가적인 회원가입을 진행시킨다.
                );
        return http.build();
    }
}
