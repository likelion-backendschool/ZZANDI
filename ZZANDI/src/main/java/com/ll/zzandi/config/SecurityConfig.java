package com.ll.zzandi.config;

import com.ll.zzandi.config.security.CustomAuthenticationProvider;
import com.ll.zzandi.config.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Autowired
    private AuthenticationSuccessHandler customAuthenticationSuccessHandler;

    /*
    인증 및 인가 설정
    */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws
            Exception {
        http
                .cors().disable()		//cors방지
                .csrf().disable()		//csrf방지
//                .formLogin().disable()	//기본 로그인 페이지 없애기
                .headers().frameOptions().disable()
                .and()

                .authorizeRequests()
                .antMatchers("/", "/user/join", "/h2-console/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/user/login")
                .loginProcessingUrl("/user/login_proc")
                .usernameParameter("userId")
                .passwordParameter("userPassword")
                .defaultSuccessUrl("/")
                .successHandler(customAuthenticationSuccessHandler)
                .permitAll()
        ;
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new CustomAuthenticationProvider();
    }
}

