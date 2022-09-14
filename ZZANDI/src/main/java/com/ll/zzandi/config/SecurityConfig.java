package com.ll.zzandi.config;

import com.ll.zzandi.config.security.CustomAccessDeniedHandler;
import com.ll.zzandi.config.security.CustomAuthenticationProvider;
import com.ll.zzandi.config.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Autowired
    private AuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler customAuthenticationFailureHandler;

    /*
    인증 및 인가 설정
    */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .cors().disable()		//cors방지
            .csrf().disable()		//csrf방지
//                .formLogin().disable()	//기본 로그인 페이지 없애기
            .headers().frameOptions().disable()
            .and()
            .authorizeRequests()
            .antMatchers("/images/**", "/js/**", "/bootstrap/**").permitAll()
            .antMatchers("/", "/user/join","/test", "/h2-console/**", "/user/login/**", "/user/check-email-token/**", "/user/denied/**", "/comment/**").permitAll()
            .antMatchers("/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/user/login")
            .loginProcessingUrl("/user/login_proc")
            .usernameParameter("userId")
            .passwordParameter("userPassword")
            .defaultSuccessUrl("/")
            .successHandler(customAuthenticationSuccessHandler)
            .failureHandler(customAuthenticationFailureHandler)
            .permitAll()
            .and()

            .exceptionHandling()
            .accessDeniedHandler(accessDeniedHandler())
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

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        CustomAccessDeniedHandler customAccessDeniedHandler = new CustomAccessDeniedHandler();
        customAccessDeniedHandler.setErrorPage("/user/denied");
        return customAccessDeniedHandler;
    }
}

